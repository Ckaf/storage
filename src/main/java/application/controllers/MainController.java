package application.controllers;

import application.entities.DataStorageEntity;
import application.tools.DatabaseManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.annotation.*;


import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
@CrossOrigin
@RestController
public class MainController {

    DatabaseManager databaseManager=new DatabaseManager();
    @RequestMapping(value="/upload", method=RequestMethod.GET, produces="text/plain")
    public @ResponseBody String provideUploadInfo() {
        return "You can upload the file using the same url.";
    }

    @ResponseBody
    @RequestMapping(value="/upload", method=RequestMethod.POST, produces="text/plain")
    public String handleFileUpload(@RequestParam(value = "name") String name,
                                   @RequestParam(value = "file") String file){

        if (!file.isEmpty()) {
            try {
                String path="storage/"+name + "-uploaded";
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(path)));
                stream.write(bytes);
                stream.close();
                DataStorageEntity dataStorage=new DataStorageEntity();
                dataStorage.setName(name);
                dataStorage.setPath(path);
                save(dataStorage);
                return "You have successfully downloaded " + name + " in " + name + "-uploaded !";
            } catch (Exception e) {
                return "You were unable to download " + name + " => " + e.getMessage();
            }
        } else {
            return "You were unable to download " + name + " because the file is empty.";
        }
    }
    public void save(DataStorageEntity dataStorageEntity) {
        Session session = databaseManager.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(dataStorageEntity);
        tx1.commit();
        session.close();
    }
}
