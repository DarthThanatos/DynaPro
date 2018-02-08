package contract;

import model.Furniture;
import model.Project;

import javax.swing.tree.TreeModel;

public interface DynProContract {

    public interface View {
        public void displayProjectPopup();
        public void displayFurniturePopup();
        public void setupProjectTreeModel(TreeModel treeModel);
        public String promptForUserInput(String message);
        void displayMetadata(String type, String name, int height, int width, int depth, int fronPrice, int moduleUnitPrice);

    }

    public interface Presenter{
        public void attachView(View view);
        public void onProjectTreePopupSelection(String name);
        public void onNewProject();
        public void onNewFurnitureAdd();
        public void onRenameProject();
        public void onRenameFurniture(String furnitureName);
        public void onRemoveFurniture(String furnitureName);
        void onMetadataSetSelected(String furnitureName);
    }

    public interface Model{
        public Project getCurrentProject();
        public Furniture getFurnitureByName(String name);
        public Project createNewProject();
        public Boolean isProject(String name);
        public void renameProject(String name);
        public Boolean addDefaultFurniture();
        public Boolean addFurniture(String name, String type);
        public Boolean renameFurniture(String oldName, String newName);
        public void removeFurniture(String name);
    }
}
