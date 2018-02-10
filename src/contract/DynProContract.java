package contract;

import model.Furniture;
import model.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.tree.TreeModel;

public interface DynProContract {

    public interface View {
        public void displayProjectPopup();
        public void displayFurniturePopup();
        public void setupProjectTreeModel(TreeModel treeModel);
        public String promptForUserInput(String message);
        public String promptForUserInput(String message, String initialValue);
        void displayMetadata(String type, String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice);

    }

    public interface Presenter{
        public void attachView();
        public void onProjectTreePopupSelection(String name);
        public void onCreateNewProject();
        public void onAddNewFurniture();
        public void onRenameProject();
        public void onRenameProjectTreeFurniture(String furnitureName);
        void onRenameMetadataFurniture();
        public void onRemoveFurniture(String furnitureName);
        void onMetadataSetSelected(String furnitureName);
        void onDisplayFurnitureMetadata(Furniture furniture);
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
    }

    public interface Model{
        public Project getCurrentProject();
        public Furniture getFurnitureByName(String name);
        public Project createNewProject();
        public Boolean isProject(String name);
        public void renameProject(String name);
        public Boolean addDefaultFurniture();
        public Boolean addFurniture(String name, String type);
        public Boolean addFurniture(String type);
        public Boolean renameFurniture(String oldName, String newName);
        public void removeFurniture(String name);
        Furniture getDefaultFurniture();
        Furniture getFurnitureWithChangedType(String name, String newType);
    }
}
