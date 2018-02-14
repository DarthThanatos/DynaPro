package model

import contract.DynProContract

class DynProModel(
        var project: Project,
        private val projectKeeper: ProjectKeeper
) : DynProContract.Model,
        Project by project,
        ProjectKeeper by projectKeeper{
    override var presenter: DynProContract.Presenter? = projectKeeper.presenter
}

