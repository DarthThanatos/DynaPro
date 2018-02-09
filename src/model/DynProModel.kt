package model

import contract.DynProContract
import presenter.FurnitureProperty
import presenter.GenericProperty
import presenter.ProjectProperty

class DynProModel : DynProContract.Model{

    private var projectProperty: ProjectProperty = ProjectProperty(DynProject())

    override fun createNewProject() : Project{
        projectProperty.set(DynProject())
        return projectProperty
    }

    override fun getDefaultFurniture(): FurnitureProperty = projectProperty.getDefaultFurniture()

    override fun getCurrentProject(): ProjectProperty = projectProperty

    override fun renameProject(name: String) { projectProperty.get().rename(name)  }

    override fun addFurniture(name: String, type: String) = projectProperty.addChildFurniture(name, type)

    override fun renameFurniture(oldName: String, newName: String) = projectProperty.renameChildFurniture(oldName, newName)

    override fun removeFurniture(name: String) { projectProperty.removeChildFurniture(name)  }

    override fun addDefaultFurniture() = projectProperty.addDefaultFurniture()

    override fun isProject(name: String) = projectProperty.isNameMine(name)

    override fun getFurnitureByName(name: String): FurnitureProperty? = projectProperty.getFurnitureByName(name)
}

