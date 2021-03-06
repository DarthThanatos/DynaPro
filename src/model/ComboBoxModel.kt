package model

import config.Config
import javax.swing.DefaultComboBoxModel

class ComboTypeModel : DefaultComboBoxModel<Any>(arrayOf<Any>(Config.UPPER_MODULE_PL, Config.BOTTOM_MODULE_PL))
class ComboFrontConfigurationModel: DefaultComboBoxModel<Any>(arrayOf<Any>(Config.ROW_ORIENTED, Config.COLUMN_ORIENTED))
class ComboBackTypeModel: DefaultComboBoxModel<Any>(arrayOf(Config.BACK_HDF, Config.BACK_INSERTED))
class ComboRoofTypeModel: DefaultComboBoxModel<Any>(arrayOf(Config.ROOF_INSERTED, Config.ROOF_NOT_INSERTED))
class ComboPedestalModel: DefaultComboBoxModel<Any>(arrayOf(Config.PEDESTAL_EXISTS, Config.NO_PEDESTAL))
class ComboFrontConfigElemTypeModel: DefaultComboBoxModel<Any>(arrayOf(Config.LEFT_DOOR_PL, Config.RIGHT_DOOR_PL, Config.EMPTY_SPACE, Config.DRAWER_PL, Config.DOUBLE_DOOR))