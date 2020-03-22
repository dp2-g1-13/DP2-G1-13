<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="persons">

    <h2>
        Change password
    </h2>
    <form:form modelAttribute="personForm" class="form-horizontal" id="add-person-form">
        <div class="form-group has-feedback">
            <flatbook:inputPassword label="Previous password" name="previousPassword"/>
            <flatbook:inputPassword label="New password" name="password"/>
            <flatbook:inputPassword label="Confirm password" name="confirmPassword"/>
            <form:hidden path="username"/>
            <form:hidden path="saveType"/>
            <form:hidden path="authority"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                 <button class="btn btn-default" type="submit">Submit</button>
            </div>
        </div>
    </form:form>

</flatbook:layout>
