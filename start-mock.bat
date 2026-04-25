@echo off
echo Starting CodeEcho in Mock Mode...
echo ============================================

set SPRING_PROFILES_ACTIVE=mock
mvn spring-boot:run

pause
