package model

import contract.DynProContract

class DynProModel : DynProContract.Model{

    lateinit var project :DynProject

    override fun createNewProject() : Project{
        project = DynProject()
        return project
    }

    override fun getCurrentProject(): DynProject = project

    override fun renameProject(name: String) { project.rename(name)  }

    override fun addFurniture(name: String, type: String) = project.addChildFurniture(name, type)

    override fun renameFurniture(oldName: String, newName: String) = project.renameChildFurniture(oldName, newName)

    override fun removeFurniture(name: String) { project.removeChildFurniture(name)  }

    override fun addDefaultFurniture() = project.addDefaultFurniture()

    override fun isProject(name: String) = project.isNameMine(name)

    override fun getFurnitureByName(name: String): Furniture? = project.getFurnitureByName(name)
}

