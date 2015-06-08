package com.jaxws.ext.handlers;

import java.util.Set;
import java.util.TreeSet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class ClientSoapMessageCaptureHandler implements SOAPHandler<SOAPMessageContext> {
  private String rawRequest;
  private String rawResponse;

  public ClientSoapMessageCaptureHandler() { 
    this.rawRequest = "";
    this.rawResponse = "";
  }

  public String getRawRequest() {
    return rawRequest;
  }

  public String getRawResponse() {
    return rawResponse;
  }

  public void processMessageContext(SOAPMessageContext context) {
    Boolean isRequest = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
    SOAPMessage soapMsg = context.getMessage();
    ByteArrayOutputStream rawOutput = new ByteArrayOutputStream();

    try {
      soapMsg.writeTo(rawOutput);

      if(isRequest.booleanValue()) {
        //Request
        this.rawRequest = rawOutput.toString();
      } else {
        // Response
        this.rawResponse = rawOutput.toString();
      }
    } catch (Exception e) {
      System.err.println(e);
      e.printStackTrace();
    }
  }

  @Override
  public boolean handleMessage(SOAPMessageContext context) {
    processMessageContext(context);
    return true;
  }

  @Override
  public boolean handleFault(SOAPMessageContext context) {
    processMessageContext(context);
    return true;
  }

  @Override
  public void close(MessageContext context) {}

  @Override
  public Set<QName> getHeaders() {
    return new TreeSet();
  }
}
