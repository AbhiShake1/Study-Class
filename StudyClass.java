import javax.swing.JOptionPane;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.*;
import java.io.File;
import java.io.FileOutputStream;

public class StudyClass{
    public static void main(String[] args){
        String className = JOptionPane.showInputDialog("Enter the full location of class that you want to inspect (Example: java.io.File)");
        String[] fClass = className.replace(".",",").split(",");
        Class mClass = null;
        try{
            if(className!=null){
                mClass = Class.forName(className);
            }
        }catch(ClassNotFoundException e){
            try{         
                if(fClass.length==1){
                    mClass = findClass(className);
                }else{
                    assert false; //throw assertion error
                }
            }catch(ClassNotFoundException | AssertionError cnfe){
                JOptionPane.showMessageDialog(null, "No such class found.\nPlease make sure you have entered the name correctly and it exists");
                reRun();
            }
        }
        if(mClass==null){
            return;
        }
        Method[] methods = mClass.getDeclaredMethods();
        Field[] fields = mClass.getDeclaredFields();
        String cls = mClass.toGenericString();
        File file = new File("StudyClass"+File.separator+fClass[fClass.length-1]+".txt");
        try{
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write("Class:\n\n"+cls+"\n\n");
            fileWriter.write("\n\nSuperclass:\n\n");
            if(mClass.isInterface()){
                Class[] interfaces = mClass.getInterfaces();
                for(Class cInterface : interfaces){
                    fileWriter.write("\n"+cInterface);
                }
            }else{
                fileWriter.write(mClass.getSuperclass().toGenericString());
            }
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

    private static Class findClass(String className) throws ClassNotFoundException{
        Class mClass;
        String[] paths = {"lang","util","io","math","lang.reflect","lang.ref",
                "lang.invoke","lang.annotation","text","time","net","nio","security",
                "util.concurrent","util.function","util.jar","util.logging","util.prefs",
                "util.regex","util.stream","util.zip","sql","time.chrono","time.format",
                "time.temporal","time.zone","awt.font","beans","nio.channels","nio.charset","nio.file","security.acel","seecurity.cert",
                "security.interfaces","security.spec"};
        //constant number of packages so time complexity will not be too high
        //cycle through all common paths to find the class from api if only the name of class is entered
        for(String path : paths){
            try{
                mClass = Class.forName("java."+path+"."+className);
                return mClass;
            }catch(ClassNotFoundException cnfe){
                continue;
            }
        }
        throw new ClassNotFoundException();
    }

    private static void reRun(){
        int again = JOptionPane.showConfirmDialog(null,"Inspect another class?");
        if(again==0) main(null);
    }
}
