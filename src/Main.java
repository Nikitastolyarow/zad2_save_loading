import java.io.*;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        GameProgress gameProgress1 = new GameProgress(79, 5, 4, 214.22);
        GameProgress gameProgress2 = new GameProgress(90, 2, 7, 284.92);
        GameProgress gameProgress3 = new GameProgress(94, 10, 12, 214.32);

        saveGame("C:/Games/savegames/progress1.dat", gameProgress1);
        saveGame("C:/Games/savegames/progress2.dat", gameProgress2);
        saveGame("C:/Games/savegames/progress3.dat", gameProgress3);

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("C:/Games/savegames/progress1.dat");
        arrayList.add("C:/Games/savegames/progress2.dat");
        arrayList.add("C:/Games/savegames/progress3.dat");

        zipFiles("C:/Games/savegames/zip.zip", arrayList);

        openZip("C:/Games/savegames/zip.zip");
        System.out.println(openProgress("C:/Games/savegames/progress1.dat"));
        System.out.println(openProgress("C:/Games/savegames/progress2.dat"));
        System.out.println(openProgress("C:/Games/savegames/progress3.dat"));

        File proDat1 = new File("C:/Games/savegames/progress1.dat");
        if (proDat1.exists()) {
            proDat1.delete();
            System.out.println("Файл progress1.dat удален");
        }
        File proDat2 = new File("C:/Games/savegames/progress2.dat");
        if (proDat2.exists()) {
            proDat2.delete();
            System.out.println("Файл progress2.dat удален");
        }
        File proDat3 = new File("C:/Games/savegames/progress3.dat");
        if (proDat3.exists()) {
            proDat3.delete();
            System.out.println("Файл progress3.dat удален");
        }
    }

    public static void saveGame(String path, GameProgress save) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // запишем экземпляр класса в файл
            oos.writeObject(save);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    public static void zipFiles(String path, ArrayList<String> arrayList) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(path))) {
            for (String zipSave : arrayList) {
                FileInputStream fis = new FileInputStream(zipSave);
                ZipEntry entry = new ZipEntry(zipSave);
                zout.putNextEntry(entry);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zout.write(buffer, 0, length);
                }
                zout.closeEntry();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openZip(String path) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(path))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName(); // получим название файла
                // распаковка
                FileOutputStream fout = new FileOutputStream(name);
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

    private static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }
}