FROM bellsoft/liberica-openjdk-debian:17


WORKDIR /work/

COPY build/libs/warehouse-0.0.1-SNAPSHOT.jar /work/warehouse-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java", "-dojar", "warehouse-0.0.1-SNAPSHOT.jar", "org.mediasoft.warehouse.WarehouseApplication"]