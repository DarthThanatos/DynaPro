package model

import config.Config
import javax.swing.DefaultComboBoxModel

class ComboTypeModel : DefaultComboBoxModel<Any>(arrayOf<Any>(Config.UPPER_MODULE, Config.BOTTOM_MODULE))
class ComboFrontConfigurationModel: DefaultComboBoxModel<Any>(arrayOf<Any>(Config.ROW_ORIENTED, Config.COLUMN_ORIENTED))
class ComboBackTypeModel: DefaultComboBoxModel<Any>(arrayOf(Config.BACK_HPV, Config.BACK_INSERTED))
class ComboRoofTypeModel: DefaultComboBoxModel<Any>(arrayOf(Config.ROOF_INSERTED, Config.ROOF_NOT_INSERTED))
class ComboPedestalModel: DefaultComboBoxModel<Any>(arrayOf(Config.PEDESTAL_EXISTS, Config.NO_PEDESTAL))