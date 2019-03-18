<!--Created by:	Colin Dyson
Student #:	7683407-->

<html>
<head>
    <title>Account Creation</title>
</head>

<body>
<h1 style="text-align: center;">Account Creation</h1>
<br>
<h3> Creating account...</h3>
<h2>
  <?php

  require ('/home/student/dysonc/.credentials.php');

  $acctnum = $_POST['acctnum'];
  if (!ctype_digit($acctnum)) {
      echo "Error: ".$acctnum." is not a valid number.";
  } else {
      $query = "SELECT * FROM dysonc_BANKACCTS WHERE acctnum = '" . $acctnum . "'";
      $update = "INSERT INTO dysonc_BANKACCTS (acctnum,acctbalance) VALUES ( '" . $acctnum . "', '0')";

      $mysqli = new mysqli();

      $mysqli->connect("localhost",$MYSQL_USER,$MYSQL_PW,"dysonc");
      if ($mysqli->errno) {
              printf("Error connecting to database: %s <br />",$mysqli->error);
              exit();
      }

      $result = $mysqli->query($query,MYSQLI_STORE_RESULT);
      if ($mysqli->errno) {
          printf("Error in query: %s <br />",$mysqli->error);
          exit();
      }

      $row = $result->fetch_row();
      if ($row[0] === NULL) {
          $result = $mysqli->query($update,MYSQLI_STORE_RESULT);
          if ($mysqli->errno) {
              printf("Error in query: %s <br />",$mysqli->error);
              exit();
          }
          echo "Account #".$acctnum." successfully created.";
      } else {
          echo "Error: An account with that number already exists.";
      }

      $mysqli->close();
  }
  ?>
</h2>
<form action="http://www3.cs.umanitoba.ca/~dysonc/bank.html">
    <button type="submit">Back to Home Page</button>
</form>
</body>
</html>
