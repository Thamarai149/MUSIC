@echo off
echo Starting Railway Reservation System...
echo.
echo Make sure MySQL is running with:
echo - Database: railway_db
echo - Username: root
echo - Password: Thamarai@2006
echo.
pause
java -cp "target/classes;lib/mysql-connector-j-8.0.33.jar" com.railway.RailwayReservationSystem
pause