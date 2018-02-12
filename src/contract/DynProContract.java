package contract;

import model.Furniture;
import model.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreeModel;

public interface DynProContract {

     interface View {
         void displayProjectPopup();
         void displayFurniturePopup();
         void setupProjectTreeModel(TreeModel treeModel);
         String promptForUserInput(String message);
         String promptForUserInput(String message, String initialValue);
         void displayMetadata(String type, String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice, String pathToImage);

    }

     interface Presenter{
        abstract void attachView();

        void onProjectTreePopupSelection(String name);
        void onCreateNewProject();
        void onAddNewFurniture();
        void onRenameProject();
        void onRenameProjectTreeFurniture(String furnitureName);
        void onRenameMetadataFurniture();
        void onRemoveFurniture(String furnitureName);
        void onMetadataSetSelected(String furnitureName);
        void onNewProjectCreated();
        void onProjectRenamed();
        void onFurnitureAdded(String addedFurnitureName);
        void onFurnitureRemoved(String removedFurnitureName);
        void onFurnitureRenamed(String oldValue, @NotNull String newValue);
        void onFurnitureTypeChanged(@NotNull String newValue);
        void onFrontUnitPriceChanged(int newValue);
        void onElementUnitPriceChanged(int newValue);
        void onFurnitureHeightChanged(int newValue);
        void onFurnitureWidthChanged(int newValue);
        void onFurnitureDepthChanged(int newValue);

        void onAddElementToFrontConfiguration(String furnitureName, int columnIndex);
        void onFrontConfigurationElementAdded(String furnitureName, int columnIndex, int newElementIndex);
        void onRemoveElementFromConfiguration(String furnitureName, int columnIndex, int elementIndex);
        void onFrontConfigurationElementRemoved(String furnitureName, int columnIndex);

    }

     interface Model{
        Project getCurrentProject();
        Furniture getFurnitureByName(String name);
        Project createNewProject();
        Boolean isProject(String name);
        void renameProject(String name);
        Boolean addDefaultFurniture();
        Boolean renameFurniture(String oldName, String newName);
        void removeFurniture(String name);
        Furniture getDefaultFurniture();
        Furniture getFurnitureWithChangedType(String name, String newType);
    }
}
