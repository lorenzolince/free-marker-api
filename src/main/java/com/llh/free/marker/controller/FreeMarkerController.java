/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.free.marker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.llh.free.marker.pdf.GeneratePdf;
import java.io.IOException;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author lorenzo
 */
@Controller
public class FreeMarkerController {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private GeneratePdf generatePdf;

    @Autowired
    private ObjectMapper mapper;

    @GetMapping("/")
    public String hello(Model model,
             @RequestParam(value = "nameTemplate") String nameTemplate,
             @RequestParam(value = "nameData") String nameData) throws IOException {
        Resource res = resourceLoader.getResource("file:./templates/" + nameData + ".json");
        Map<String, Object> data = mapper.readValue(res.getFile(), Map.class);
        model.addAllAttributes(data);
        return nameTemplate;
    }

    @RequestMapping(value = "/pdf", method
            = RequestMethod.GET, produces = "application/pdf")
    public ResponseEntity<byte[]> electionBookPdf(@RequestParam(value = "nameTemplate") String nameTemplate,
             @RequestParam(value = "nameData") String nameData) throws IOException {
        Resource res = resourceLoader.getResource("file:./templates/" + nameTemplate + ".ftl");
        String template = IOUtils.toString(res.getInputStream(), UTF_8);
        res = resourceLoader.getResource("file:./templates/" + nameData + ".json");
        Map<String, Object> data = mapper.readValue(res.getFile(), Map.class);
        byte[] pdf = generatePdf.generatePdf(template, data, nameTemplate);
        return ResponseEntity
                .ok()
                .contentLength(pdf.length)
                .contentType(
                        MediaType.parseMediaType("application/pdf"))
                .body(pdf);
    }
}
