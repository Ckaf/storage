package application.controllers;

import application.entities.DataStorageEntity;
import application.tools.DatabaseManager;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

@Controller
public class MainController {

    DatabaseManager databaseManager=new DatabaseManager();
    @RequestMapping(value="/upload", method=RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "You can upload the file using the same url.";
    }

    @RequestMapping(value="/upload", method=RequestMethod.POST)
    public @ResponseBody ResponseEntity handleFileUpload(@RequestParam(value = "name") String name,
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
                return ResponseEntity.accepted().body("You have successfully downloaded " + name + " in " + name + "-uploaded !");
            } catch (Exception e) {
                return ResponseEntity.accepted().body("You were unable to download " + name + " => " + e.getMessage());
            }
        } else {
            return ResponseEntity.accepted().body("You were unable to download " + name + " because the file is empty.");
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
