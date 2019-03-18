<!--Created by:	Colin Dyson
Student #:	7683407-->

<html>
<head>
    <title>Account Deposit</title>
</head>

<body>
<h1 style="text-align: center;">Account Deposit</h1>
<br>
<h3> Retrieving account...</h3>
<h2>
  <?php

  require ('/home/student/dysonc/.credentials.php');

  $acctnum = $_POST['acctnum'];
  $deposit = $_POST['deposit'];

  $query = "SELECT * FROM dysonc_BANKACCTS WHERE acctnum = '" . $acctnum . "'";

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
      echo "Error: No account exists with number {$acctnum}.";
  } else {
      $newbalance = $row[1] + $deposit;
      $update = "UPDATE dysonc_BANKACCTS SET acctbalance = {$newbalance} WHERE acctnum = {$acctnum}";
      $result = $mysqli->query($update,MYSQLI_STORE_RESULT);
      if ($mysqli->errno) {
          printf("Error in query: %s <br />",$mysqli->error);
          exit();
      }
      $num = $row[0];
      echo "Account #".$num." Balance: $".$newbalance;
      echo " Deposit Successful.";
  }

  $mysqli->close();

  ?>
</h2>
</body>
</html>
