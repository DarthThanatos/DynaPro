package model

import contract.DynProContract
import kotlin.properties.Delegates

class DynProModel(private val presenter: DynProContract.Presenter) : DynProContract.Model{
    override fun getFurnitureWithChangedType(name: String, newType: String): Furniture = project.getFurnitureWithChangedType(name, newType)


    private var project: DynProject by Delegates.observable(DynProject(presenter) ){property, oldValue, newValue -> presenter.onNewProjectCreated() }

    override fun createNewProject() : Project{
        project = DynProject(presenter)
        return project
    }

    override fun getDefaultFurniture(): Furniture = project.getDefaultFurniture()

    override fun getCurrentProject(): Project = project

    override fun renameProject(name: String) { project.rename(name)  }


    override fun renameFurniture(oldName: String, newName: String) = project.renameChildFurniture(oldName, newName)

    override fun removeFurniture(name: String) { project.removeChildFurniture(name)  }

    override fun addDefaultFurniture() = project.addDefaultFurniture()

    override fun isProject(name: String) = project.isNameMine(name)

    override fun getFurnitureByName(name: String): Furniture? = project.getFurnitureByName(name)
}
