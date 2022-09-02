package com.example.demo.controllers;

import com.example.demo.dto.TicketCreatedDto;
import com.example.demo.dto.TicketResponseDto;
import com.example.demo.dto.UserHolder;
import com.example.demo.services.facade.FileUpload;
import com.example.demo.services.facade.TicketService;
import com.example.demo.services.impl.ZohoDeskService;
import io.minio.errors.MinioException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    @Autowired
    UserHolder user;

    @GetMapping("")
    //@RolesAllowed("admin")
    public List<TicketResponseDto> getTickets(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ticketService.findAll(pageNo, pageSize);
    }


    @PostMapping("")
    public ResponseEntity<?> save(
            @RequestBody() TicketCreatedDto ticketCreatedDto) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(ticketService.save(ticketCreatedDto, user.getUsername()));

    }

    @GetMapping("/token/{code}")
    //@RolesAllowed("admin")
    public void getToken(@PathVariable("code") String code) throws Exception {
        zohoDeskService.getAuthToken(code);
    }

    @GetMapping("/token")
    public void getAccessToken() throws Exception {

        zohoDeskService.getAccessToken();
    }


    @GetMapping("/{id}")
    //@RolesAllowed({"user"})
    public TicketResponseDto findById(@PathVariable("id") String id) {

        return ticketService.findById(id);
    }


    @GetMapping("/user")
    //@RolesAllowed({"user", "admin"})
    public List<TicketResponseDto> findByUser(
            @RequestParam(defaultValue = "0") Integer pageNo,
            @RequestParam(defaultValue = "10") Integer pageSize
    ) {
        return ticketService.findByUser(user.getUsername(), pageNo, pageSize);
    }

    @DeleteMapping("/{id}")
    //@RolesAllowed("user")
    public void delete(@PathVariable() String id) {
        ticketService.delete(id);
    }


    @PutMapping("/{id}")
    //@RolesAllowed("user")
    public TicketResponseDto update(@RequestBody() TicketCreatedDto ticketCreatedDto, @PathVariable() String id) throws ChangeSetPersister.NotFoundException {
        return ticketService.update(ticketCreatedDto, id);
    }

    //upload to minio
    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        String uuid = UUID.randomUUID().toString();
        String message = "";
        try {
            fileUpload.putObject(uuid, file);
            message = "Uploaded the file successfully: " + file.getOriginalFilename();
            return ResponseEntity.status(HttpStatus.OK).body(message);

        } catch (MinioException e) {
            throw new IllegalStateException("The file cannot be upload on the internal storage. Please retry later", e);
        } catch (IOException e) {
            throw new IllegalStateException("The file cannot be read", e);
        }
    }

    //download from minio
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

    @PostMapping("/attachement/{ticketId}")
    public String attachFileToTicket(@RequestParam("file") MultipartFile file, @PathVariable("ticketId") String ticketId) throws Exception {
        return zohoDeskService.attachFileToATicket(ticketId, file);
    }

    //get ticket by status
    @GetMapping("/status/{status}")
    public List<TicketResponseDto> getTicketByStatus(@PathVariable("status") String status) {
        return ticketService.findByStatus(status);
    }

}
