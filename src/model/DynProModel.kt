package model

import contract.DynProContract
import kotlin.properties.Delegates

class DynProModel(private val presenter: DynProContract.Presenter) : DynProContract.Model{

    private var project: DynProject by Delegates.observable(DynProject(presenter) ){property, oldValue, newValue -> presenter.onProjectChanged() }

    override fun createNewProject() : Project{
        project = DynProject(presenter)
        return project
    }

    override fun getDefaultFurniture(): Furniture = project.getDefaultFurniture()

    override fun getCurrentProject(): Project = project

    override fun renameProject(name: String) { project.rename(name)  }

    override fun addFurniture(name: String, type: String) = project.addChildFurniture(name, type)

    override fun renameFurniture(oldName: String, newName: String) = project.renameChildFurniture(oldName, newName)

    override fun removeFurniture(name: String) { project.removeChildFurniture(name)  }

    override fun addDefaultFurniture() = project.addDefaultFurniture()

    override fun isProject(name: String) = project.isNameMine(name)

    override fun getFurnitureByName(name: String): Furniture? = project.getFurnitureByName(name)
}
