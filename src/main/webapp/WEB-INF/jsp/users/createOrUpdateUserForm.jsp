<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<flatbook:layout pageName="persons">

    <h2>
        User
    </h2>
    <form:form modelAttribute="personForm" class="form-horizontal" id="add-person-form">
        <div class="form-group has-feedback">
            <c:if test="${personForm.saveType == 'NEW'}" >
            	<flatbook:inputField label="Username" name="username" />
            	<flatbook:selectField size="2" label="User Type" name="authority" names="${types}"/>
            	<flatbook:inputPassword label="Password" name="password"/>
         		<flatbook:inputPassword label="Confirm Password" name="confirmPassword"/>
            </c:if>
            <c:if test="${personForm.saveType == 'EDIT'}" >
                <flatbook:inputFieldDisabled label="Username" name="username" />
            	<form:hidden path="authority"/>
            </c:if>
            <flatbook:inputField label="First Name" name="firstName"/>
            <flatbook:inputField label="Last Name" name="lastName"/>
            <flatbook:inputField label="Dni" name="dni"/>
            <flatbook:inputField label="Email" name="email"/>
            <flatbook:inputField label="Phone Number" name="phoneNumber"/>
            
            <form:hidden path="saveType"/>
        </div>
        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-10">
                 <button class="btn btn-default" type="submit">Submit</button>
            </div>
        </div>
    </form:form>

</flatbook:layout>
