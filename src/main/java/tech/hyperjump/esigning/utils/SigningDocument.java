package tech.hyperjump.esigning.utils;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfReader;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SigningDocument {

    public static String addUserSigning(String pdfSource, String pdfDest, String signImageName, String userName) {
        try {
            String dest = "uploads/".concat(pdfDest);
            PdfDocument pdfDoc = new PdfDocument(new PdfReader(pdfSource), new PdfWriter(dest));
            int index = pdfDoc.getNumberOfPages();
            PageSize ps = new PageSize(pdfDoc.getFirstPage().getPageSize());
            float width = ps.getWidth();
            float height = ps.getHeight();
            float vertical_space = 20;
            float sign_height = 50;
            float sign_width = 200;
            float left_space = 50;
            float top_space = 150;
            float name_height = 20;

            PdfPage lastpage = pdfDoc.addNewPage(index + 1, ps);
            PdfCanvas pdfCanvas = new PdfCanvas(lastpage);
            Rectangle rectangle = new Rectangle(36, height - 200, 100, 100);
            pdfCanvas.rectangle(rectangle);

            PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);

            // canvas untuk gambar tanda tangan
            Rectangle rect_sign = new Rectangle(left_space, height - top_space, sign_width, sign_height);
            Canvas signcanvas = new Canvas(pdfCanvas, rect_sign);
            // Creating an ImageData object
            String imFile = "uploads/".concat(signImageName);
            ImageData data = ImageDataFactory.create(imFile);
            System.out.println(rect_sign.getY());

            // Creating an Image object
            Image image = new Image(data);
            image.setAutoScale(false);
            signcanvas.add(image);
            signcanvas.close();

            // Creating a Document
            //Document document = new Document(pdfDoc);

            Rectangle rect_name = new Rectangle(left_space, height - top_space - sign_height, sign_width, name_height);
            System.out.println(rect_name.getY());
            Canvas namecanvas = new Canvas(pdfCanvas, rect_name);
            Text nameText = new Text(userName).setFont(bold);
            Paragraph nameParagraph = new Paragraph().add(nameText);
            namecanvas.add(nameParagraph);
            // lastpage.ad

            namecanvas.close();

            // Adding image to the document
            // document.add(image);

            // Closing the document
            //document.close();
            pdfDoc.close();
            System.out.println("PDF Created");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return pdfDest;
    }
}
