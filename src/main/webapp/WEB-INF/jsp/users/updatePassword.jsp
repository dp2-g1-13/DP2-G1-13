<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="persons">

    
    <div class="container col-md-8" align="center">
    <form:form modelAttribute="message" class="form-horizontal" id="add-person-form">
        <div class="form-group has-feedback">
            <flatbook:textAreaField label="body" name="previousPassword"/>
            <form:hidden path="sender.username"/>
            <form:hidden path="receiver.username"/>
            <form:hidden path="creationDate"/>
            <button class="btn btn-default" type="submit">Submit</button>
        </div>
    </form:form>
    </div>

</flatbook:layout>
