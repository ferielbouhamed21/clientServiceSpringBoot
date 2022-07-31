package com.example.demo.controllers;

import com.example.demo.dto.TicketControllerDto;
import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.services.facade.FileUpload;
import com.example.demo.services.facade.TicketService;
import com.example.demo.services.impl.ZohoDeskService;
import io.minio.errors.MinioException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("tickets")

public class TicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private ZohoDeskService zohoDeskService;
    @Autowired
    private FileUpload fileUpload;

    @GetMapping("")
    //@RolesAllowed("admin")
    public List<TicketResponseDto> getTickets() {

        return ticketService.findAll();
    }
    @PostMapping("")
   /* public TicketResponseDto save(
            @RequestBody() TicketCreatedDto ticketCreatedDto,MultipartFile file) throws Exception {*/
        public TicketResponseDto save(
            @ModelAttribute()TicketControllerDto ticketControllerDto) throws Exception {
        return ticketService.save(ticketControllerDto);
    }

    @GetMapping("/token/{code}")
    //@RolesAllowed("admin")
    public void getToken(@PathVariable("code") String code) throws Exception {
         System.out.println(code);
         zohoDeskService.getAuthToken(code);

    }
    @GetMapping("/token")
    public void getAccessToken() throws Exception {

         zohoDeskService.getAccessToken();

    }


    @GetMapping("/{id}")
    @RolesAllowed({"user"})
    public TicketResponseDto findById(@PathVariable("id") Integer id) {

        return ticketService.findById(id);
    }


    @GetMapping("/user/{id}")
    @RolesAllowed({"user", "admin"})
    public List<TicketResponseDto> findByUser(@PathVariable() Integer id) {
        return ticketService.findByUser(id);
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("user")
    public void delete(@PathVariable() Integer id) {
        ticketService.delete(id);
    }


    @PutMapping("/{id}")
    @RolesAllowed("user")
    public TicketResponseDto update(@RequestBody() TicketCreatedDto ticketCreatedDto, @PathVariable() Integer id) throws ChangeSetPersister.NotFoundException {
        return ticketService.update(ticketCreatedDto, id);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file ) throws Exception{
        String uuid = UUID.randomUUID().toString();
        String message = "";
        try {
            fileUpload.putObject(uuid,file);
             message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);

        } catch (MinioException e) {
        throw new IllegalStateException("The file cannot be upload on the internal storage. Please retry later", e);
        } catch (IOException e) {
        throw new IllegalStateException("The file cannot be read", e);
        }
    }

    @GetMapping("/download/{fileId}")
    public void getObject(@PathVariable("fileId") String fileId, HttpServletResponse response) throws MinioException, IOException {
        InputStream inputStream = fileUpload.getObject(fileId);

        // Set the content type and attachment header.
        response.addHeader("Content-disposition", "attachment;filename=" + fileId);
        response.setContentType(URLConnection.guessContentTypeFromName(fileId));

        // Copy the stream to the response's output stream.
        IOUtils.copy(inputStream, response.getOutputStream());
        response.flushBuffer();
    }
    @PostMapping("/attachement/{fileId}")
    public String attachFileToTicket(@RequestParam("file") MultipartFile file ,@PathVariable("fileId") String fileId) throws Exception{
        return zohoDeskService.attachFileToATicket(fileId,file);
    }

}
