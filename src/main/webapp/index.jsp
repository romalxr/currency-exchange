<html>
<head>
    <script>
        function sendPostRequest() {
            const formData = new FormData();
            formData.append("name", "Tenge");
            formData.append("code", "KZT");
            formData.append("sign", "₸");

            fetch("/currencies", {
                method: "POST",
                body: new URLSearchParams(formData),
                headers: {
                    "Content-Type": "application/x-www-form-urlencoded"
                }
            })
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    throw new Error("Request failed with status: " + response.status);
                }
            })
            .then(data => {
                alert("Currency added successfully: " + JSON.stringify(data));
            })
            .catch(error => {
                alert("Error: " + error.message);
            });
        };
        function deleteCurrency() {
            const code = "KZT";

            fetch("/currency/KZT", {
                method: "DELETE"
            })
            .then(response => {
                if (response.ok) {
                    alert("Currency with code " + code + " deleted successfully.");
                } else if (response.status === 404) {
                    alert("Currency with code " + code + " not found.");
                } else {
                    throw new Error("Request failed with status: " + response.status);
                }
            })
            .catch(error => {
                alert("Error: " + error.message);
            });
        }
    </script>
</head>
<body>
<h2>Hello World 2!</h2>
<button onclick="sendPostRequest()">Test1 post</button>
<button onclick="deleteCurrency()">Test2 delete</button>
</body>
</html>
