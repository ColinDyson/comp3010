<!--Created by:	Colin Dyson
Student #:	7683407-->

<html>
<head>
    <title>Account Withdrawal</title>
</head>

<body>
<h1 style="text-align: center;">Account Withdrawal</h1>
<br>
<h3> Retrieving account...</h3>
<h2>
  <?php

  require ('/home/student/dysonc/.credentials.php');

  $acctnum = $_POST['acctnum'];
  $withdrawal = $_POST['withdrawal'];

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
  } else if ($row[1] < $withdrawal){
      echo "Error: Failed to withdraw $".$withdrawal.": Insufficient Funds.";
      echo " Account #".$row[0]." Balance: $".$row[1];
  } else {
      $newbalance = $row[1] - $withdrawal;
      $update = "UPDATE dysonc_BANKACCTS SET acctbalance = {$newbalance} WHERE acctnum = {$acctnum}";
      $result = $mysqli->query($update,MYSQLI_STORE_RESULT);
      if ($mysqli->errno) {
          printf("Error in query: %s <br />",$mysqli->error);
          exit();
      }
      $num = $row[0];
      echo "Account #".$num." Balance: $".$newbalance;
      echo " Withdrawal Successful.";
  }

  $mysqli->close();

  ?>
</h2>
</body>
</html>
