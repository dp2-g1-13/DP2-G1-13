<%@ page session="false" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!-- %@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %-->  

<flatbook:layout pageName="home">
    <h2><fmt:message key="welcome"/></h2>
    <div class="row">
        <div class="col-md-12">
            <form:form modelAttribute="address" action="/advertisements" method="get" class="form-horizontal"
                       id="search-advertisement-form">
                <div class="form-group">
                    <h4>Looking for a flat?</h4><br>
                    <div class="control-group" id="city">
                        <label class="col-sm-2 control-label">City </label>
                        <div class="col-sm-10">
                            <form:input class="form-control" path="city" size="30" maxlength="80" placeholder="City, Country"/>
                            <span class="help-inline"><form:errors path="city"/></span>
                        </div>
                    </div>
                    <div class="control-group" id="postalCode">
                        <label class="col-sm-2 control-label">Postal code </label>
                        <div class="col-sm-10">
                            <form:input class="form-control" path="postalCode" size="30" maxlength="80"/>
                            <span class="help-inline"><form:errors path="postalCode"/></span>
                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-default">Find your new home!</button>
                    </div>
                </div>

            </form:form>
        </div>
    </div>
</flatbook:layout>
