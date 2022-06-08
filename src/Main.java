import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;



public class Main {
    public static void main(String[] args) {
        GameProgress progress1 = new GameProgress(96, 25, 23, 200);
        GameProgress progress2 = new GameProgress(100, 42, 27, 265);
        GameProgress progress3 = new GameProgress(25, 25, 4, 67);

        GameProgress[] gameProgress = new GameProgress[3];
        gameProgress[0] = progress1;
        gameProgress[1] = progress2;
        gameProgress[2] = progress3;

        File save1 = new File("C://Users//i.grachev//IdeaProjects//Games//savegames//save1.dat");
        File save2 = new File("C://Users//i.grachev//IdeaProjects//Games//savegames//save2.dat");
        File save3 = new File("C://Users//i.grachev//IdeaProjects//Games//savegames//save3.dat");

        ArrayList<String> list = new ArrayList<>();
        list.add(save1.getAbsolutePath());
        list.add(save2.getAbsolutePath());
        list.add(save3.getAbsolutePath());

        saveGame(list, gameProgress);

        zipFiles("C://Users//i.grachev//IdeaProjects//Games//savegames//zip.zip", list);

        deleteFile(save1, save1.getAbsolutePath());
        deleteFile(save2, save2.getAbsolutePath());
        deleteFile(save3, save3.getAbsolutePath());

        openZip("C://Users//i.grachev//IdeaProjects//Games//savegames//zip.zip",
                "C://Users//i.grachev//IdeaProjects//Games//savegames//");

        System.out.println(getProgress(save1.getAbsolutePath()));


    }

    public static void saveGame(ArrayList arrayList, GameProgress[] gameProgresses) {
        for (int i = 0; i < arrayList.size(); i++) {
            try (FileOutputStream fos = new FileOutputStream((String) arrayList.get(i));
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                for (int b = 0; b < gameProgresses.length; b++) {
                    oos.writeObject(gameProgresses[b]);
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }

    }
    public static void zipFiles(String way, ArrayList arrayList) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(way))) {
            for (int i = 0; i < arrayList.size(); i++) {
                FileInputStream fis = new FileInputStream((String) arrayList.get(i));
                ZipEntry entry = new ZipEntry("save" + (i+1) + ".dat");
                zout.putNextEntry(entry);
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();
                fis.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void deleteFile(File save, String address) {
        if (save.delete()) {
            System.out.println("Файл " + save + " был удален");
        } else {
            System.out.println("Файл " + save + " не был удален");
        }

    }
    public static void openZip(String way, String dir) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(way))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream(dir + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static GameProgress getProgress(String address) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(address);
        ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}