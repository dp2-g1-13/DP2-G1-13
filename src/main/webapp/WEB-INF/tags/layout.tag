<%@ tag trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="flatbook" tagdir="/WEB-INF/tags" %>

<%@ attribute name="pageName" required="true" %>
<%@ attribute name="customScript" required="false" fragment="true"%>

<!doctype html>
<html>
<flatbook:htmlHeader/>

<body>
<flatbook:bodyHeader menuName="${pageName}"/>

<div class="container-fluid">
    <div class="container xd-container">

        <jsp:doBody/>

        <flatbook:pivotal/>
    </div>
</div>
<flatbook:footer/>
<jsp:invoke fragment="customScript" />

</body>

</html>
