<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" content="text/html; charset=UTF-8"/>
        <title>${name}</title>
        <link href=${resourcePath}/test.css rel="stylesheet"  type = "text/css"/>
        <script type="text/javascript" src="${resourcePath}/test.js"></script>
    </head>
    <body>
        <#import "test.macro" as PdfMacros>
        <#global pdfTitle = "${name}" >
        <#global pdfCode = "0147">
        <#global pdfStatus = "Draft">
        <#global pdfVersion = "1.0">

        <#global generate_date = "${.now?string.medium}" >

        <#-- COVER -->
        <@PdfMacros.footer pageCode="Cover Code">
        </@PdfMacros.footer>

        <@PdfMacros.fixedPagesSection pageNumber=1 contentText="&nbsp;">
        <@PdfMacros.header>
        </@PdfMacros.header>
        <div id="cover">
            <div id="content">
                <h2 class="title">Code ${code}</h2>
                <h2 class="title">Name ${name}</h2>
                <h2 class="title">Direction ${direction.name}</h2>
                <h2 class="title">Street ${direction.street}</h2>
            </div>
        </div>
        <#-- TABLE TOP -->
        <div class="table-top">
            <div class="left">
                <p>
                    ${name}<br />
                    friends .................. :
                </p>
            </div>

            <div class="right">
                <p>
                    <br />
                    <br />
                    ${code}
                </p>
            </div>
        </div>
        <#-- TABLE DEFINITION -->
        <table>

            <#-- HEADER OF TABLE -->
            <thead>
                <tr>
                    <th class="left">
                        Name
                    </th>
                    <th class="left">
                        tell
                    </th>
                    <th class="left">
                        email
                    </th>
                </tr>
            </thead>
            <#--BODY OF TABLES-->
            <tbody>
                <#list friends as frend >
                <tr>
                    <td class="left">
                        ${frend.name}
                    </td>
                    <td class="left">
                        ${frend.tel}
                    </td>
                    <td class="left">
                        ${frend.email}
                    </td>
                </tr>

                </#list>

            </tbody>
        </table>
        <#global pageBreakCount = pageBreakCount + 1>
        <#global currentPage += 1>
        </@PdfMacros.fixedPagesSection>
        <#-- END COVER -->
    </body>
</html>
