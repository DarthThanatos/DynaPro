package config;

public class Config {
    public final static String UPPER_MODULE = "Upper Module";
    public final static String UPPER_MODULE_PL = "Korpus Górny";

    public final static String BOTTOM_MODULE = "Bottom Module";
    public final static String BOTTOM_MODULE_PL = "Korpus Dolny";

    public final static String NEW_PROJECT_PL = "Nowy Projekt";
    public final static String NEW_UPPER_MODULE_PL = "Nowy górny korpus";

    public final static int MAX_DEFAULT_FURNITURE_NAMES_NUMBER = Integer.MAX_VALUE;
    public final static String DEFAULT_FURNITURE_NAME_PREFIX_PL = "Nowy mebel ";

    public final static String GIVE_NEW_PROJECT_NAME_MSG = "Podaj nową nazwę projektu (nie może być pusta)";
    public final static String GIVE_NEW_FURNITURE_NAME_MSG = "Podaj nową nazwę mebla (nie może być pusta)";
    public final static String GIVE_FURNITURE_TYPE = "Podaj typ mebla";

    public final static String CURRENT_FURNITURE = "current furniture";
    public final static String PROJECT_TREE_SUB = "project tree sub";
    public final static String META_PRESENTER_SUB = "meta presenter sub";

    public final static String DRAWER_PL = "szuflada";
    public final static String LEFT_DOOR_PL = "lewe drzwiczki";
    public final static String RIGHT_DOOR_PL = "prawe drzwiczki";
    public final static String EMPTY_SPACE = "pusta przestrzeń";

    public static final String ROW_ORIENTED = "Zorientowany wierszami";
    public static final String COLUMN_ORIENTED = "Zorientowany kolumnami";

    public static final String BACK_INSERTED = "Wkładane";
    public static final String BACK_HDF = "Przybijane";

    public static final String ROOF_NOT_INSERTED = "Nakładany";
    public static final String ROOF_INSERTED = "Wkładany";

    public static final String PEDESTAL_EXISTS = "Jest";
    public static final String NO_PEDESTAL = "Brak (ignorowana wprowadzana wartość po prawej)";
    public static final String FRONT_CONFIG_ELEM_DIALOG_TITLE_PL = "Podaj charakterystykę elementu:";

    public static final String YES_PL = "Tak", NO_PL = "Nie";
    public static final String FRONT_CONFIG_ELEMENT_TIP_FORMAT = "<html>Nazwa: %s<br/>Typ: %s<br/>Szerokość: %d<br/>Wysokość: %d<br/>Szerokość zablokowana: %s<br/> Wysokość zablokowana: %s<br/>Orientacja słojów wzdłuż wysokości: %s<br/>Ilość dodatkowych półek: %d</html>";

    public static final int SLAB_WIDTH = 18, BETWEEN_ELEMENTS_HORIZONTAL_GAP = 3, BETWEEN_ELEMENTS_VERTICAL_GAP = 2;

    public static float MESH_UNIT = 250.0f;
    public static int HDF_BACK_THICKNESS = 3;
}
