<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="persons">

    <h2>
        
    </h2>
    <div class="col-s-8" align="center">
    <c:forEach items="${messages}" var="mess">
            <div align="left">
                	<div class="messagelist" > <c:out value="${mess.sender.username}"/></div>
                
                   <div class="messagelist" > <c:out value="${mess.body}"/></div>
                
                	<flatbook:localDatetime date="${mess.creationMoment}"/>
                	</div>
                
        </c:forEach>
    <form:form action="/messages/${message.receiver.username}/new" modelAttribute="message" class="form-horizontal" id="add-person-form">
            <form:hidden path="receiver.username"/>
            <flatbook:inputField label="" name="body"/>
            <form:hidden path="creationMoment"/>
            <form:hidden path="sender.username"/>
            <div class="col-sm-offset-2 col-sm-10">
                 <button class="btn btn-default" type="submit">Send</button>
            </div>
    </form:form>
    </div>

</flatbook:layout>
