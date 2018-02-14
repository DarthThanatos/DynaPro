package model

import config.Config
import contract.DynProContract
import kotlin.properties.Delegates

interface ProjectKeeper{
    var presenter: DynProContract.Presenter?
    fun isProject(name: String) : Boolean
    fun getCurrentProject(): Project
    fun renameProject(name: String)
    fun createNewProject() : Project
}

class DynaProjectKeeper(initialProject: Project): ProjectKeeper{

    override var presenter: DynProContract.Presenter?=null
    private var project: Project by Delegates.observable( initialProject ){ property, oldValue, newValue -> presenter?.onNewProjectCreated() }

    override fun getCurrentProject(): Project = project

    override fun isProject(name: String) = project.isNameMine(name)
    override fun renameProject(name: String) { project.rename(name)  }

    override fun createNewProject() : Project{
        project.reset(Config.NEW_PROJECT_PL, presenter!!)
        return project
    }
}