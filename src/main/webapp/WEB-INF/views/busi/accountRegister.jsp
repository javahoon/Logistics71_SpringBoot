<%--
  Created by IntelliJ IDEA.
  User: 82109
  Date: 2022-09-29
  Time: 오전 11:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script src="https://unpkg.com/ag-grid-community/dist/ag-grid-community.min.noStyle.js"></script>
    <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-grid.css">
    <link rel="stylesheet" href="https://unpkg.com/ag-grid-community/dist/styles/ag-theme-balham.css">
    <script src="${pageContext.request.contextPath}/js/modal.js?v=<%=System.currentTimeMillis()%>" defer></script>
    <script src="${pageContext.request.contextPath}/js/datepicker.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/datepickerUse.js" defer></script>
    <script src="${pageContext.request.contextPath}/js/modal.js?v=<%=System.currentTimeMillis()%>" defer></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/datepicker.css">

</head>
<body>
<div>
    <h1>거래처 목록</h1>
    <article class="estimateGrid">
        <div align="center">
            <div id="accountData" class="ag-theme-balham" style="height:450px; width:auto; text-align: center;"></div>
        </div>
    </article>


    <script>
        $.ajax({
            url: "/compinfo/customer/allList",     // 데이터를 받아올 경로
            dataType: "json",                       // json형태로 받아오기 위함
            method: "GET",                          //GET 메소드는 주로 데이터를 읽거나(Read) 검색(Retrieve)할 때에 사용
                                                    //POST 메소드는 주로 새로운 리소스를 생성(create)할 때 사용
            success: (obj) => {                     // url의 경로가 성공적이면 실행
                console.log(obj.customer+"abvced");
                estGridOptions.api.setRowData(obj.customer)
                // estGridOptions.api.setRowData()는 데이터를 교체하는것이라고함 , 빈배열에 컨트롤러에서 가져온 값들을 넣기위함
            }
        })

        const accoutGrid = document.querySelector("#accountData"); // grid의 이름 그리고 값을 넣을 div의 id

        let estColumn = [
            {headerName: "고객 코드", field: "customerCode"}, // editable: 편집가능한 문자열로 EditText 의 기본 Type , field는 변수명
            {headerName: "직장코드", field: "workplaceCode"},
            {headerName: "고객 이름", field: "customerName"},
            {headerName: "고객유형", field: "customerType"},
            {headerName: "고객CEO", field: "customerCeo"},
            {headerName: "비즈니스 라이선스 번호", field: "businessLicenseNumber"},
            {headerName: "사회보장번호", field: "socialSecurityNumber"},
            {headerName: "고객비즈니스조건", field: "customerBusinessConditions"},
            {headerName: "고객비즈니스아이템", field: "customerBusinessItems"},
            {headerName: "고객우편번호", field: "customerZipCode"},
            {headerName: "고객기본주소", field: "customerBasicAddress"},
            {headerName: "고객 정보 주소", field: "customerDetailAddress"},
            {headerName: "고객전화번호", field: "customerTelNumber"},
            {headerName: "고객 팩스 번호", field: "customerFaxNumber"},
            {headerName: "고객노트", field: "customerNote"},
            {headerName: "생산된 제품", field: "producedProduct"}

        ];

        let estRowData = [];     // 빈 배열을 넣음

        // event.colDef.field
        let estGridOptions = {
            columnDefs: estColumn,  // 열을 정의하는거 columnDefs
            rowSelection: 'single', //row data를 선택하는 경우의 옵션으로 하나만 선택
            rowData: estRowData,  // 그리드에 표시할 데이터를 설정
            onGridReady: function (event) { // 그리드가 시작하자마자 실행되는거
                event.api.sizeColumnsToFit(); //자동으로 글자나 이것저것 크기조정
            }
        }
        document.addEventListener('DOMContentLoaded', () => {  // DOMContentLoaded : 스크립트가 시작할 준비가 됐으면 function 함수실행
            new agGrid.Grid(accoutGrid, estGridOptions);        // accoutGrid는 div태그의 변수이름이고 , estGridOptions는 안에 들어갈 값들이다
        })
    </script>


    <article class="register">
        <div align="center">
            <div id="registerAccount" class="ag-theme-balham"
                 style="height:100px; width:auto; text-align: center;"></div>
        </div>
    </article>
    <button onclick="f()">추가하기시작</button>
    <button onclick="ff()">추가하기</button>
    <script>
        const registerAccount = document.querySelector("#registerAccount");
        let registerEstColumn = [
            { checkboxSelection: true,headerCheckboxSelectionFilteredOnly: true,headerCheckboxSelection: true}, // 어떤 열을 보낼지 선택하도록 체크박스 추가
            {headerName: "고객 코드", field: "customerCode", editable: true,}, // editable: 편집가능한 문자열로 EditText 의 기본 Type , field는 변수명
            {headerName: "직장코드", field: "workplaceCode", editable: true},
            {headerName: "고객 이름", field: "customerName", editable: true},
            {headerName: "고객유형", field: "customerType", editable: true},
            {headerName: "고객CEO", field: "customerCeo", editable: true},
            {headerName: "비즈니스 라이선스 번호", field: "businessLicenseNumber", editable: true},
            {headerName: "사회보장번호", field: "socialSecurityNumber", editable: true},
            {headerName: "고객비즈니스조건", field: "customerBusinessConditions", editable: true},
            {headerName: "고객비즈니스아이템", field: "customerBusinessItems", editable: true},
            {headerName: "고객우편번호", field: "customerZipCode", editable: true},
            {headerName: "고객기본주소", field: "customerBasicAddress", editable: true},
            {headerName: "고객 정보 주소", field: "customerDetailAddress", editable: true},
            {headerName: "고객전화번호", field: "customerTelNumber", editable: true},
            {headerName: "고객 팩스 번호", field: "customerFaxNumber", editable: true},
            {headerName: "고객노트", field: "customerNote", editable: true},
            {headerName: "생산된 제품", field: "producedProduct"}
        ];

        let row = {
            customerCode: "",
            workplaceCode: "",
            customerName: "",
            customerType: "",
            customerCeo: "",
            businessLicenseNumber: "",
            socialSecurityNumber: "",
            customerBusinessConditions: "",
            customerBusinessItems: "",
            customerZipCode: "",
            customerBasicAddress: "",
            customerDetailAddress: "",
            customerTelNumber: "",
            customerFaxNumber: "",
            customerNote: "",
            producedProduct: ""
        };

       function f() {
            registerOptions.api.updateRowData({add: [row]}); // 버튼 클릭시 option에 row의 값을 업데이트한다
        }


        let registerOptions = {
            columnDefs: registerEstColumn,  // 열을 정의하는거 columnDefs
            autoSizeColumn: estColumn,      // 자동으로 칼럼들을 사이즈조정해줌
            rowData: estRowData,            // 그리드에 표시할 데이터를 설정
            onGridReady: function (event) { // 그리드가 시작하자마자 실행되는거
                event.api.sizeColumnsToFit();//자동으로 글자나 이것저것 크기조정
            },
            onGridSizeChanged: function (event) { // 창크기가 변경되면 실행되는 이벤트
                event.api.sizeColumnsToFit();
            },
            getSelectedRowData() {
                let selectedNodes = this.api.getSelectedNodes();     // Object 찍힘
                let selectedData = selectedNodes.map(node => node.data); // Object 찍힘
                return selectedData;
            }
        }
        function ff() {
                let xhr = new XMLHttpRequest(); /*  XMLHttpRequest는 HTTP를 통해서 쉽게 데이터를 받을 수 있게 해주는 오브젝트를 제공한다
                                                    Ajax로 실행되는 HTTP 통신도 XMLHttpRequest규격을 이용함  */
            // XHR을 사용하면 페이지의 새로고침 없이도 URL에서 데이터를 가져올 수 있습니다
                xhr.open('GET', "/compinfo/customer/registerAccount?"   // 요청을 초기화합니다 , URL 경로
                    + "toList=" + encodeURI(JSON.stringify(registerOptions.getSelectedRowData()))  // 열에 입력된 문자를 인코딩된 문자열로 바꿈
                    , true)
                xhr.setRequestHeader('Accept', 'application/json');  // HTTP 요청 헤더의 값을 설정합니다. 반드시 send()보다 먼저, 그러나 open()보다 뒤에 호출해야함
                xhr.send();                    // 요청을 전송합니다. 비동기 요청(기본 동작)인 경우, send()는 요청을 전송하는 즉시 반환
                xhr.onreadystatechange = () => {                    // readyState 속성이 바뀔 때마다 발생합니다. onreadystatechange 속성으로도 수신할 수 있음
                    if (xhr.readyState == 4 && xhr.status == 200) {
                        let txt = xhr.responseText;
                        console.log(txt);
                        txt = JSON.parse(txt);
                        if (txt.errorCode < 0) {
                            swal.fire("오류", txt.errorMsg, "error");
                            return;
                        }
                        Swal.fire(
                            '성공!',
                            'success'
                        )
                        console.log(txt);


                        registerOptions.api.setRowData([]);  // 문자열들을 배열에 담음

                    }
                }
                location.reload();   // 클릭하면 다시 로드
            }
          /*  location.href("/compinfo/customer/registerAccount?"
                + "toList=" + encodeURI(JSON.stringify(registerOptions.getSelectedRowData())));*/
           // console.log(encodeURI(JSON.stringify(registerOptions.getSelectedRowData())));




        document.addEventListener('DOMContentLoaded', () => {   // 위와 같음
            new agGrid.Grid(registerAccount, registerOptions);
        })
    </script>
</div>
</body>
</html>
