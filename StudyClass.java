import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.io.File;

public class StudyClass{
    public static void main(String[] args){
        String className = JOptionPane.showInputDialog("Enter the full location of class that you want to inspect (Example: java.io.File)");
        Class mClass = null;
        try{
            if(className!=null){
                mClass = Class.forName(className);
            }
        }catch(ClassNotFoundException e){
            try{
                //prevent confusion between java.lang.String and String for beginners as java.lang package does not need to be imported
                mClass = Class.forName("java.lang."+className);
            }catch(ClassNotFoundException cnfe){
                JOptionPane.showMessageDialog(null, "No such class found.\nPlease make sure you have entered the name correctly and it exists");
                reRun();
            }
        }
        if(mClass==null){
            return;
        }
        Method[] methods = mClass.getDeclaredMethods();
        Field[] fields = mClass.getDeclaredFields();
        String[] fClass = className.replace(".",",").split(",");
        File file = new File("StudyClass"+File.separator+fClass[fClass.length-1]+".txt");
        try{
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("Superclass:\n\n");
            fileWriter.write(mClass.getGenericSuperclass().toString());
            fileWriter.write("\n\n\nMethods:\n\n\n");
            for(Method method : methods){
                fileWriter.write(method.toString()+"\n\n");
            }
            fileWriter.write("\n\n\nVariables:\n\n\n");
            for(Field field : fields){
                fileWriter.write(field.toString()+"\n\n");
            }
            JOptionPane.showMessageDialog(null, "list of methods and variables has been saved to "+file);
            fileWriter.close();
            reRun();
        }catch (IOException ioe){
            JOptionPane.showMessageDialog(null, "Failed to write to file. "+ioe.getMessage());
        }
    }

    private static final void reRun(){
        int again = JOptionPane.showConfirmDialog(null,"Inspect another class?");
        if(again==0) main(null);
    }
}