package model

import config.Config
import javax.swing.DefaultComboBoxModel

class ComboTypeModel : DefaultComboBoxModel<Any>(arrayOf<Any>(Config.UPPER_MODULE, Config.BOTTOM_MODULE))
class ComboFrontConfigurationModel: DefaultComboBoxModel<Any>(arrayOf<Any>(Config.ROW_ORIENTED, Config.COLUMN_ORIENTED))