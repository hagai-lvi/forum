<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<body>


<form:form method="POST" action="ex2" commandName="Example">
      <table>
        <tr>
              <td>First Name:</td>
              <td><form:input path="username" /></td>
          </tr>
          <tr>
              <td>Last Name:</td>
              <td><form:input path="password" /></td>
          </tr>
          <tr>
              <td colspan="2">
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>
      </table>
  </form:form>

<form:form method="POST" action="ex2" commandName="Example">
      <table>
        <tr>
              <td>First Name:</td>
              <td><form:input path="username" /></td>
          </tr>
          <tr>
              <td>Last Name:</td>
              <td><form:input path="password" /></td>
          </tr>
          <tr>
              <td colspan="2">
                  <input type="submit" value="Save Changes" />
              </td>
          </tr>
      </table>
  </form:form>
</body>
</html>