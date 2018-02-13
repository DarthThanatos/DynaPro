package contract;

import model.FrontConfigurationVM;
import model.Furniture;
import model.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.tree.TreeModel;

public interface DynProContract {

     interface View {
         void displayProjectPopup();
         void displayFurniturePopup();
         void setupProjectTreeModel(TreeModel treeModel);
         String promptForUserInput(String message);
         String promptForUserInput(String message, String initialValue);
         void displayMetadata(String type, String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice, String pathToImage);
         void displaySpecifics(String pedestalOptionText, String backOptionText, String roofOptionText);
         void displayFrontConfiguration(FrontConfigurationVM frontConfigurationVM, String configOrientationText);

         void displayFrontConfigurationRowOrientedPopup(String elementId);
         void displayFrontConfigurationColumnOrientedPopup(String elementId);

    }

     interface Presenter{
        void attachView();

        void onProjectTreePopupSelection(String name);
        void onCreateNewProject();
        void onAddNewFurniture();
        void onRenameProject();
        void onRenameProjectTreeFurniture(String furnitureName);
        void onRenameMetadataFurniture();
        void onRemoveFurniture(String furnitureName);
        void onFurnitureSelected(String furnitureName);
        void onNewProjectCreated();
        void onProjectRenamed();
        void onFurnitureAdded(String addedFurnitureName);
        void onFurnitureRemoved(String removedFurnitureName);
        void onFurnitureRenamed(String oldValue, @NotNull String newValue);

        void onAddElementToFrontConfiguration(String furnitureName, int columnIndex);
        void onFrontConfigurationElementAdded(String furnitureName, int columnIndex, int newElementIndex);
        void onRemoveElementFromConfiguration(String furnitureName, String elementId);
        void onFrontConfigurationElementRemoved(String furnitureName, int columnIndex);

        void onFurnitureTypeChanged(@NotNull String newValue);
        void onChooseFurnitureConfigurationPopup(String furnitureName, String elementId);
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

         void removeFrontElementFromFurniture(String furnitureName, String elementId);
     }
}
