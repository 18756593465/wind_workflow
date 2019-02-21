package com.bcx.wind.workflow.helper;

import com.bcx.wind.workflow.exception.WorkflowException;
import com.bcx.wind.workflow.parser.XMLProcessEntityResolver;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.io.StringReader;

public class XmlHelper {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(XmlHelper.class);

    public static Document buildDoc(String context) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new XMLProcessEntityResolver());
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            InputSource source = new InputSource(new StringReader(context));
            return builder.parse(source);
        } catch (Exception e) {

            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
            throw new WorkflowException(e);
        }
    }


    public static Document buildDoc(InputStream stream) {
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(true);

            factory.setNamespaceAware(false);
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(false);
            factory.setCoalescing(false);
            factory.setExpandEntityReferences(true);

            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setEntityResolver(new XMLProcessEntityResolver());
            builder.setErrorHandler(new ErrorHandler() {
                @Override
                public void error(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void fatalError(SAXParseException exception) throws SAXException {
                    throw exception;
                }

                @Override
                public void warning(SAXParseException exception) throws SAXException {
                }
            });
            InputSource source = new InputSource(stream);
            return builder.parse(source);
        } catch(Exception e){

            if (logger.isDebugEnabled()) {
                logger.debug(e.getMessage(), e);
            }
            throw new WorkflowException(e);
        }
    }


}
