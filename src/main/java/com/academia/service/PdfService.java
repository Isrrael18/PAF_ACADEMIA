package com.academia.service;

import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.itextpdf.html2pdf.HtmlConverter;

@Service
public class PdfService {
	@Autowired
    private SpringTemplateEngine templateEngine;
	
	public byte[] createPdf(String templateName, Context context) {
        try {
        	String htmlContent = templateEngine.process("reporte/" + templateName, context);
        	
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            HtmlConverter.convertToPdf(htmlContent, baos);
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
