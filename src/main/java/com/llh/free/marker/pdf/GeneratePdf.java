/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.llh.free.marker.pdf;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.template.Configuration;
import static freemarker.template.Configuration.VERSION_2_3_23;
import freemarker.template.Template;
import static freemarker.template.TemplateExceptionHandler.RETHROW_HANDLER;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import static java.nio.charset.StandardCharsets.UTF_8;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.util.Locale;
import java.util.Map;
import org.springframework.stereotype.Service;
import freemarker.cache.StringTemplateLoader;
import freemarker.cache.TemplateLoader;
import java.io.File;

/**
 *
 * @author lorenzolince
 */
@Service
public class GeneratePdf {

    private volatile static Configuration configuration;
    private Template cache = null;
    private static final String PATH_TEMPLATES = "./templates";
    private static StringTemplateLoader stringTemplateLoader;

    static {

        if (configuration == null) {
            synchronized (GeneratePdf.class) {
                if (configuration == null) {
                    configuration = new Configuration(VERSION_2_3_23);
                }
            }
            configuration.setDefaultEncoding(UTF_8.name());
            configuration.setLocale(Locale.US);

            try {
                FileTemplateLoader ftlTemplateLoader
                        = new FileTemplateLoader(new File(PATH_TEMPLATES));

                MultiTemplateLoader multiTemplateLoader
                        = new MultiTemplateLoader(new TemplateLoader[]{stringTemplateLoader, ftlTemplateLoader});
                configuration.setTemplateLoader(multiTemplateLoader);

                configuration.setCacheStorage(
                        new freemarker.cache.MruCacheStorage(10, 20));
            } catch (Exception e) {
                e.printStackTrace();

            }
            configuration.setDefaultEncoding("UTF-8");
            configuration.setTemplateExceptionHandler(RETHROW_HANDLER);
        }

    }

    public byte[] generatePdf(String data, Map<String, Object> model, String namePdf) {
        String html;
        String xhtml;

        try {
            html = this.createHtml(data, model, namePdf);
            xhtml = this.toXhtml(html);

            return this.toPdf(xhtml);

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private String createHtml(String data, Map<String, Object> model, String namePdf) {
        StringWriter out;
        try {
            Template template = new Template(namePdf, new StringReader(data), configuration);
            out = new StringWriter();
            template.process(model, out);
            return out.toString();
        } catch (Exception e) {
        }
        return null;
    }

    private String toXhtml(String html) {
        ByteArrayInputStream in;
        ByteArrayOutputStream out;
        Tidy tidy;
        try {
            tidy = new Tidy();
            tidy.setInputEncoding(UTF_8.name());
            tidy.setOutputEncoding(UTF_8.name());
            tidy.setXHTML(true);
            tidy.setTrimEmptyElements(false);
            tidy.setShowWarnings(true);
            tidy.setQuiet(true);

            in = new ByteArrayInputStream(html.getBytes(UTF_8));
            out = new ByteArrayOutputStream();

            tidy.parseDOM(in, out);
            return out.toString(UTF_8.name());
        } catch (Exception e) {
        }
        return null;
    }

    private byte[] toPdf(String xhtml) {
        ByteArrayOutputStream baos;
        try {
            ITextRenderer renderer;

            renderer = new ITextRenderer();
            renderer.getFontResolver().addFont("Code39.ttf", true);
            renderer.setDocumentFromString(xhtml);
            renderer.layout();

            baos = new ByteArrayOutputStream();
            renderer.createPDF(baos);

            return baos.toByteArray();
        } catch (Exception e) {
        }
        return null;
    }
}
