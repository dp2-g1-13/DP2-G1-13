<%@ tag body-content="empty" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>

<%@ tag import="java.util.Base64" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ attribute name="data" required="true" rtexprvalue="true" type="java.lang.Byte[]"
              description="Binary data of the image" %>
<%@ attribute name="fileType" required="true" rtexprvalue="true"
              description="The type of the image" %>
<%@ attribute name="height" required="false" rtexprvalue="true" type="java.lang.Integer"
              description="Height of the image in pixels" %>
<%@ attribute name="width" required="false" rtexprvalue="true" type="java.lang.Integer"
              description="Width of the image in pixels" %>
<%@ attribute name="cssClass" required="false" rtexprvalue="true"
              description="Corresponds to the class atribute in img tag" %>

<%
    Byte[] tmp = (Byte[]) jspContext.getAttribute("data");
    byte[] byteData = new byte[tmp.length];
    int i = 0;
    for(Byte b : tmp) {
        byteData[i] = b.byteValue();
        i++;
    }
    String imageEncoded = Base64.getEncoder().encodeToString(byteData);
    jspContext.setAttribute("imageEncoded", imageEncoded);
%>

<c:if test="${(height != null and width == null) or (height == null and width != null)}">
    <c:remove var="height"/>
    <c:remove var="width"/>
</c:if>

<img alt="sample" src="data:${fileType};base64,${imageEncoded}"<c:if test="${height != null}"> height="${height}"</c:if><c:if test="${width != null}"> width="${width}"</c:if><c:if test="${cssClass != null}"> class="${cssClass}"</c:if>>
