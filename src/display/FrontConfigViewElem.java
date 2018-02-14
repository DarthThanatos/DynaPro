package display;

public class FrontConfigViewElem extends ImageButton {

    private FrontConfigurationDisplayer frontConfigurationDisplayer;
    private String modelKey, furnitureName;

    FrontConfigViewElem(String imagePath, String modelKey, String furnitureName){
        super(imagePath);
        this.modelKey = modelKey;
        this.furnitureName = furnitureName;
    }

    void setFrontConfigurationDisplayer(FrontConfigurationDisplayer frontConfigurationDisplayer){
        this.frontConfigurationDisplayer = frontConfigurationDisplayer;
    }

    public void notifyParentAboutChange(){
        frontConfigurationDisplayer.onChildElemChanged(this);
    }

    public String getFurnitureName() {
        return furnitureName;
    }

    public String getModelKey(){
        return modelKey;
    }
}
