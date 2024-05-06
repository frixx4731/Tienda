-- MySQL dump 10.13  Distrib 8.0.36, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: ingsoftware
-- ------------------------------------------------------
-- Server version	8.0.36

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `alertas`
--

DROP TABLE IF EXISTS `alertas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `alertas` (
  `AlertaID` int NOT NULL AUTO_INCREMENT,
  `ProductoID` int DEFAULT NULL,
  `TipoAlerta` varchar(50) DEFAULT NULL,
  `Mensaje` text,
  `FechaEmitida` datetime DEFAULT NULL,
  PRIMARY KEY (`AlertaID`),
  KEY `ProductoID` (`ProductoID`),
  CONSTRAINT `alertas_ibfk_1` FOREIGN KEY (`ProductoID`) REFERENCES `productos` (`ProductoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `alertas`
--

LOCK TABLES `alertas` WRITE;
/*!40000 ALTER TABLE `alertas` DISABLE KEYS */;
/*!40000 ALTER TABLE `alertas` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `area`
--

DROP TABLE IF EXISTS `area`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `area` (
  `AreaID` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(255) DEFAULT NULL,
  `Descripcion` text,
  PRIMARY KEY (`AreaID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `area`
--

LOCK TABLES `area` WRITE;
/*!40000 ALTER TABLE `area` DISABLE KEYS */;
INSERT INTO `area` VALUES (1,'Salchichonería',NULL),(2,'Frutas y Verduras',NULL),(3,'Panadería',NULL),(4,'Galletas y Cereales',NULL),(5,'Bebidas',NULL),(6,'Productos de Limpieza',NULL),(7,'Botanas',NULL),(8,'Cuidado Personal',NULL),(9,'Congelados',NULL);
/*!40000 ALTER TABLE `area` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configuracionroles`
--

DROP TABLE IF EXISTS `configuracionroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `configuracionroles` (
  `Rol` varchar(50) NOT NULL,
  `ContraseñaHash` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`Rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configuracionroles`
--

LOCK TABLES `configuracionroles` WRITE;
/*!40000 ALTER TABLE `configuracionroles` DISABLE KEYS */;
INSERT INTO `configuracionroles` VALUES ('ADMINISTRADOR','8d4ea62f1f99b8e1f4fe03b23d1d1eeb02443c17084900a99d6c6261d5c82327'),('GERENTE','7ce65c7cae3b41c02411558583e88cae4ee09b38baec2ff7b9c7fd16bdaf70af'),('SUPERVISOR','4e08e72a3dfafc1e69b2beae48cee39a2868bb00589efa36db7bc276ddd96449');
/*!40000 ALTER TABLE `configuracionroles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detallesventa`
--

DROP TABLE IF EXISTS `detallesventa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detallesventa` (
  `DetalleVentaID` int NOT NULL AUTO_INCREMENT,
  `VentaID` int DEFAULT NULL,
  `ProductoID` int DEFAULT NULL,
  `Cantidad` int DEFAULT NULL,
  `PrecioUnitario` decimal(10,2) DEFAULT NULL,
  `PrecioTotal` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`DetalleVentaID`),
  KEY `VentaID` (`VentaID`),
  KEY `ProductoID` (`ProductoID`),
  CONSTRAINT `detallesventa_ibfk_1` FOREIGN KEY (`VentaID`) REFERENCES `ventas` (`VentaID`),
  CONSTRAINT `detallesventa_ibfk_2` FOREIGN KEY (`ProductoID`) REFERENCES `productos` (`ProductoID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detallesventa`
--

LOCK TABLES `detallesventa` WRITE;
/*!40000 ALTER TABLE `detallesventa` DISABLE KEYS */;
/*!40000 ALTER TABLE `detallesventa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `productos`
--

DROP TABLE IF EXISTS `productos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `productos` (
  `ProductoID` int NOT NULL AUTO_INCREMENT,
  `Nombre` varchar(255) DEFAULT NULL,
  `Descripcion` text,
  `AreaID` int DEFAULT NULL,
  `Precio` decimal(10,2) DEFAULT NULL,
  `UnidadesDisponibles` int DEFAULT NULL,
  `NivelReorden` int DEFAULT NULL,
  `FechaCaducidad` date DEFAULT NULL,
  `CodigoBarras` varchar(255) DEFAULT NULL,
  `TamañoNeto` varchar(255) DEFAULT NULL,
  `Marca` varchar(255) DEFAULT NULL,
  `Contenido` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ProductoID`),
  UNIQUE KEY `CodigoBarras` (`CodigoBarras`),
  KEY `CategoriaID` (`AreaID`),
  CONSTRAINT `fk_productos_area` FOREIGN KEY (`AreaID`) REFERENCES `area` (`AreaID`)
) ENGINE=InnoDB AUTO_INCREMENT=2557 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `productos`
--

LOCK TABLES `productos` WRITE;
/*!40000 ALTER TABLE `productos` DISABLE KEYS */;
INSERT INTO `productos` VALUES (2046,'Salchicha de pavo',NULL,1,47.80,22,NULL,NULL,'10001',NULL,'FUD','8 salchichas'),(2047,'Salchicha de Res',NULL,1,55.00,30,NULL,NULL,'10002',NULL,'San Rafael','Paquete de 1kg'),(2048,'Salchicha de Cerdo',NULL,1,40.50,25,NULL,NULL,'10003',NULL,'Zwan','Paquete de 750g'),(2049,'Queso Manchego',NULL,1,62.30,40,NULL,NULL,'10004',NULL,'Lala','Pieza de 500g'),(2050,'Queso Oaxaca',NULL,1,48.70,35,NULL,NULL,'10005',NULL,'Sargento','Paquete de 1kg'),(2051,'Jamón de Pavo',NULL,1,45.90,20,NULL,NULL,'10006',NULL,'Zwan','Paquete de 300g'),(2052,'Salami',NULL,1,50.20,38,NULL,NULL,'10007',NULL,'La Villita','Paquete de 200g'),(2053,'Chorizo Español',NULL,1,65.50,42,NULL,NULL,'10008',NULL,'El Mero','Paquete de 400g'),(2054,'Chorizo Argentino',NULL,1,55.90,28,NULL,NULL,'10009',NULL,'Rica','Paquete de 600g'),(2055,'Jamoneta de Pavo',NULL,1,44.00,25,NULL,NULL,'10010',NULL,'San Rafael','Paquete de 350g'),(2056,'Salchichas Frankfurt',NULL,1,42.50,30,NULL,NULL,'10011',NULL,'Zwan','Paquete de 10 unidades'),(2057,'Salchichón',NULL,1,48.00,20,NULL,NULL,'10012',NULL,'Villa María','Paquete de 250g'),(2058,'Salchicha de Tofu',NULL,1,38.50,36,NULL,NULL,'10013',NULL,'H-E-B','Paquete de 400g'),(2059,'Salchichas Viena',NULL,1,55.00,40,NULL,NULL,'10014',NULL,'FUD','Paquete de 12 unidades'),(2060,'Queso Panela',NULL,1,58.90,30,NULL,NULL,'10015',NULL,'Lala','Pieza de 1kg'),(2061,'Jamón de Pierna',NULL,1,45.00,22,NULL,NULL,'10016',NULL,'San Rafael','Paquete de 500g'),(2062,'Queso Gouda',NULL,1,72.50,18,NULL,NULL,'10017',NULL,'El Mero','Pieza de 750g'),(2063,'Salchichas Rancheras',NULL,1,47.00,35,NULL,NULL,'10018',NULL,'Zwan','Paquete de 8 unidades'),(2064,'Salchichas de Ternera',NULL,1,59.50,27,NULL,NULL,'10019',NULL,'Rica','Paquete de 400g'),(2065,'Queso Asadero',NULL,1,52.00,33,NULL,NULL,'10020',NULL,'Lala','Paquete de 500g'),(2066,'Chorizo Argentino Picante',NULL,1,65.00,20,NULL,NULL,'10021',NULL,'Rica','Paquete de 500g'),(2067,'Jamón de Pavo Bajo en Grasa',NULL,1,40.00,15,NULL,NULL,'10022',NULL,'Zwan','Paquete de 200g'),(2068,'Queso Cotija',NULL,1,62.00,28,NULL,NULL,'10023',NULL,'Lala','Pieza de 300g'),(2069,'Salchichas de Pavo',NULL,1,43.50,25,NULL,NULL,'10024',NULL,'San Rafael','Paquete de 400g'),(2070,'Salchichas de Res al Chipotle',NULL,1,57.80,40,NULL,NULL,'10025',NULL,'FUD','Paquete de 6 unidades'),(2071,'Salchichas de Cerdo con Queso',NULL,1,49.90,35,NULL,NULL,'10026',NULL,'Zwan','Paquete de 500g'),(2072,'Queso Crema',NULL,1,45.00,20,NULL,NULL,'10027',NULL,'Philadelphia','Paquete de 250g'),(2073,'Jamón Serrano',NULL,1,60.50,25,NULL,NULL,'10028',NULL,'La Española','Paquete de 300g'),(2074,'Salchichas de Tofu Ahumadas',NULL,1,35.50,18,NULL,NULL,'10029',NULL,'H-E-B','Paquete de 350g'),(2075,'Salchichas de Pavo con Jalapeño',NULL,1,47.00,30,NULL,NULL,'10030',NULL,'San Rafael','Paquete de 450g'),(2076,'Queso Fresco',NULL,1,54.00,40,NULL,NULL,'10031',NULL,'Los Altos','Pieza de 500g'),(2077,'Chorizo Español Extra',NULL,1,69.00,15,NULL,NULL,'10032',NULL,'El Mero','Paquete de 300g'),(2078,'Jamón de Pavo en Rebanadas',NULL,1,42.50,22,NULL,NULL,'10033',NULL,'Zwan','Paquete de 200g'),(2079,'Salchichas de Res con Chipotle',NULL,1,58.80,28,NULL,NULL,'10034',NULL,'FUD','Paquete de 400g'),(2080,'Salchichas de Cerdo a la Parrilla',NULL,1,45.20,32,NULL,NULL,'10035',NULL,'Zwan','Paquete de 350g'),(2081,'Queso Cheddar',NULL,1,67.00,24,NULL,NULL,'10036',NULL,'Kraft','Paquete de 450g'),(2082,'Salchichas de Ternera con Hierbas',NULL,1,63.50,40,NULL,NULL,'10037',NULL,'Rica','Paquete de 500g'),(2083,'Salchichas de Pavo con Queso',NULL,1,49.00,30,NULL,NULL,'10038',NULL,'San Rafael','Paquete de 600g'),(2084,'Salchichas de Res con Mostaza',NULL,1,56.70,20,NULL,NULL,'10039',NULL,'FUD','Paquete de 300g'),(2085,'Salchichas de Cerdo con Tocino',NULL,1,52.30,25,NULL,NULL,'10040',NULL,'Zwan','Paquete de 400g'),(2086,'Queso Provolone',NULL,1,70.00,32,NULL,NULL,'10041',NULL,'El Mero','Pieza de 600g'),(2087,'Chorizo Argentino Extra',NULL,1,67.50,18,NULL,NULL,'10042',NULL,'Rica','Paquete de 450g'),(2088,'Jamón de Pavo Extra Magro',NULL,1,43.00,24,NULL,NULL,'10043',NULL,'Zwan','Paquete de 250g'),(2089,'Salchichas de Tofu a la Barbacoa',NULL,1,39.50,35,NULL,NULL,'10044',NULL,'H-E-B','Paquete de 500g'),(2090,'Salchichas de Pavo con Chile',NULL,1,45.00,28,NULL,NULL,'10045',NULL,'San Rafael','Paquete de 350g'),(2091,'Queso Gruyere',NULL,1,65.00,20,NULL,NULL,'10046',NULL,'El Mero','Paquete de 400g'),(2092,'Salchichas de Res con Chipotle',NULL,1,59.80,32,NULL,NULL,'10047',NULL,'FUD','Paquete de 450g'),(2093,'Salchichas de Cerdo con Queso',NULL,1,48.50,27,NULL,NULL,'10048',NULL,'Zwan','Paquete de 550g'),(2094,'Queso Havarti',NULL,1,56.00,22,NULL,NULL,'10049',NULL,'Los Altos','Pieza de 500g'),(2095,'Chorizo Argentino',NULL,1,63.00,30,NULL,NULL,'10050',NULL,'Rica','Paquete de 500g'),(2096,'Jamón de Pavo Extra Ahumado',NULL,1,49.80,18,NULL,NULL,'10051',NULL,'Zwan','Paquete de 350g'),(2097,'Jamón de Pavo Bajo en Grasa',NULL,1,42.00,20,NULL,NULL,'10052',NULL,'San Rafael','Paquete de 300g'),(2098,'Jamón de Pavo Ahumado en Rebanadas',NULL,1,45.00,22,NULL,NULL,'10053',NULL,'FUD','Paquete de 250g'),(2099,'Jamón de Pavo en Rodajas',NULL,1,39.50,25,NULL,NULL,'10054',NULL,'Zwan','Paquete de 200g'),(2100,'Jamón de Pavo Bajo en Sodio',NULL,1,44.50,30,NULL,NULL,'10055',NULL,'San Rafael','Paquete de 350g'),(2101,'Cecina Natural',NULL,1,62.00,20,NULL,NULL,'10056',NULL,'El Mexicano','Paquete de 500g'),(2102,'Cecina Adobada',NULL,1,58.50,18,NULL,NULL,'10057',NULL,'Carnes Gómez','Paquete de 400g'),(2103,'Tasajo',NULL,1,55.00,15,NULL,NULL,'10058',NULL,'Carnicería San José','Paquete de 600g'),(2104,'Tocino Ahumado',NULL,1,48.00,25,NULL,NULL,'10059',NULL,'Smithfield','Paquete de 300g'),(2105,'Tocino de Pavo',NULL,1,42.00,28,NULL,NULL,'10060',NULL,'Zwan','Paquete de 250g'),(2106,'Queso Gouda',NULL,1,65.00,30,NULL,NULL,'10061',NULL,'El Mero','Pieza de 600g'),(2107,'Queso Havarti',NULL,1,58.00,35,NULL,NULL,'10062',NULL,'Los Altos','Pieza de 500g'),(2108,'Queso Cheddar Añejo',NULL,1,70.00,22,NULL,NULL,'10063',NULL,'Lala','Pieza de 750g'),(2109,'Queso Cotija',NULL,1,55.00,20,NULL,NULL,'10064',NULL,'El Mexicano','Pieza de 400g'),(2110,'Queso de Bola Edam',NULL,1,72.00,18,NULL,NULL,'10065',NULL,'El Mero','Pieza de 800g'),(2111,'Queso Manchego Viejo',NULL,1,65.00,15,NULL,NULL,'10066',NULL,'Los Altos','Pieza de 500g'),(2112,'Queso de Oveja',NULL,1,68.00,20,NULL,NULL,'10067',NULL,'Castañeda','Pieza de 600g'),(2113,'Queso de Cabra',NULL,1,60.00,25,NULL,NULL,'10068',NULL,'El Mero','Pieza de 450g'),(2114,'Queso Azul',NULL,1,63.00,28,NULL,NULL,'10069',NULL,'Lala','Pieza de 400g'),(2115,'Queso Gruyère',NULL,1,67.00,20,NULL,NULL,'10070',NULL,'Los Altos','Pieza de 550g'),(2116,'Queso Feta',NULL,1,58.00,22,NULL,NULL,'10071',NULL,'El Mexicano','Pieza de 350g'),(2117,'Queso Roquefort',NULL,1,65.00,18,NULL,NULL,'10072',NULL,'El Mero','Pieza de 300g'),(2118,'Queso Parmesano',NULL,1,60.00,20,NULL,NULL,'10073',NULL,'Lala','Pieza de 450g'),(2119,'Queso Provolone',NULL,1,62.00,25,NULL,NULL,'10074',NULL,'Los Altos','Pieza de 500g'),(2120,'Queso Edam',NULL,1,65.00,30,NULL,NULL,'10075',NULL,'El Mero','Pieza de 600g'),(2121,'Queso Emmental',NULL,1,68.00,22,NULL,NULL,'10076',NULL,'Los Altos','Pieza de 400g'),(2122,'Queso de Cabra Curado',NULL,1,63.00,18,NULL,NULL,'10077',NULL,'Castañeda','Pieza de 350g'),(2123,'Queso Mozzarella',NULL,1,60.00,20,NULL,NULL,'10078',NULL,'Lala','Pieza de 450g'),(2124,'Queso de Búfala',NULL,1,65.00,25,NULL,NULL,'10079',NULL,'El Mero','Pieza de 550g'),(2125,'Queso Fresco',NULL,1,58.00,28,NULL,NULL,'10080',NULL,'Los Altos','Pieza de 500g'),(2126,'Queso Panela',NULL,1,62.00,30,NULL,NULL,'10081',NULL,'El Mexicano','Pieza de 600g'),(2127,'Queso Crema',NULL,1,48.00,22,NULL,NULL,'10082',NULL,'Philadelphia','Paquete de 300g'),(2128,'Queso Cottage',NULL,1,55.00,20,NULL,NULL,'10083',NULL,'Lala','Paquete de 400g'),(2129,'Queso Ricotta',NULL,1,60.00,25,NULL,NULL,'10084',NULL,'El Mexicano','Paquete de 500g'),(2130,'Queso Asadero',NULL,1,57.00,28,NULL,NULL,'10085',NULL,'Lala','Paquete de 450g'),(2131,'Queso Panela Light',NULL,1,58.00,30,NULL,NULL,'10086',NULL,'El Mexicano','Paquete de 600g'),(2132,'Queso Oaxaca',NULL,1,50.00,22,NULL,NULL,'10087',NULL,'Lala','Paquete de 350g'),(2133,'Queso Cotija',NULL,1,58.00,28,NULL,NULL,'10088',NULL,'El Mexicano','Paquete de 400g'),(2134,'Queso Reblochon',NULL,1,65.00,20,NULL,NULL,'10089',NULL,'Los Altos','Paquete de 500g'),(2135,'Queso Gouda Ahumado',NULL,1,68.00,25,NULL,NULL,'10090',NULL,'El Mero','Paquete de 550g'),(2136,'Queso Camembert',NULL,1,70.00,30,NULL,NULL,'10091',NULL,'Los Altos','Paquete de 600g'),(2137,'Queso Cheddar',NULL,1,58.00,22,NULL,NULL,'10092',NULL,'Lala','Paquete de 450g'),(2138,'Queso Manchego',NULL,1,62.00,28,NULL,NULL,'10093',NULL,'El Mexicano','Paquete de 400g'),(2139,'Queso Azul Gourmet',NULL,1,65.00,32,NULL,NULL,'10094',NULL,'Los Altos','Paquete de 500g'),(2140,'Queso de Cabra a las Finas Hierbas',NULL,1,68.00,35,NULL,NULL,'10095',NULL,'Castañeda','Paquete de 600g'),(2141,'Queso Brie',NULL,1,60.00,25,NULL,NULL,'10096',NULL,'Los Altos','Paquete de 450g'),(2142,'Queso Emmental Light',NULL,1,55.00,20,NULL,NULL,'10097',NULL,'El Mero','Paquete de 400g'),(2143,'Queso Gruyère Viejo',NULL,1,65.00,28,NULL,NULL,'10098',NULL,'Los Altos','Paquete de 500g'),(2144,'Queso de Búfala Light',NULL,1,62.00,30,NULL,NULL,'10099',NULL,'El Mero','Paquete de 550g'),(2145,'Queso Mozzarella en Lonchas',NULL,1,50.00,22,NULL,NULL,'10100',NULL,'Lala','Paquete de 300g'),(2146,'Manzana Gala',NULL,2,25.00,100,NULL,NULL,'20001',NULL,'Productores Unidos','1 kg'),(2147,'Plátano Dominico',NULL,2,15.00,150,NULL,NULL,'20002',NULL,'Bananera Nacional','1 kg'),(2148,'Naranja Valencia',NULL,2,20.00,120,NULL,NULL,'20003',NULL,'Cítricos del Sur','1 kg'),(2149,'Pera Bartlett',NULL,2,30.00,80,NULL,NULL,'20004',NULL,'Productores Unidos','1 kg'),(2150,'Fresa Fresca',NULL,2,12.00,200,NULL,NULL,'20005',NULL,'Fresuras S.A.','250 g'),(2151,'Piña Golden',NULL,2,18.00,90,NULL,NULL,'20006',NULL,'Frutas del Caribe','1 unidad'),(2152,'Melón Cantalupo',NULL,2,22.00,70,NULL,NULL,'20007',NULL,'Frutas del Campo','1 unidad'),(2153,'Sandía Dulce',NULL,2,25.00,60,NULL,NULL,'20008',NULL,'Sandier S.A.','1 unidad'),(2154,'Uva Roja',NULL,2,35.00,120,NULL,NULL,'20009',NULL,'Frutas del Valle','500 g'),(2155,'Durazno Amarillo',NULL,2,28.00,80,NULL,NULL,'20010',NULL,'Cosecha Fresca','1 kg'),(2156,'Kiwi Importado',NULL,2,40.00,100,NULL,NULL,'20011',NULL,'Exóticos S.A.','1 kg'),(2157,'Mango Ataulfo',NULL,2,20.00,150,NULL,NULL,'20012',NULL,'Mangos del Pacífico','1 kg'),(2158,'Aguacate Hass',NULL,2,50.00,90,NULL,NULL,'20013',NULL,'Aguacateros Unidos','1 kg'),(2159,'Tomate Saladette',NULL,2,18.00,200,NULL,NULL,'20014',NULL,'Hortalizas Nacionales','1 kg'),(2160,'Pepino Cohombro',NULL,2,12.00,120,NULL,NULL,'20015',NULL,'Hortalizas del Campo','1 kg'),(2161,'Zanahoria Dulce',NULL,2,15.00,150,NULL,NULL,'20016',NULL,'Hortalizas Nacionales','1 kg'),(2162,'Pimiento Rojo',NULL,2,30.00,100,NULL,NULL,'20017',NULL,'Hortalizas del Campo','1 kg'),(2163,'Calabacín Verde',NULL,2,25.00,80,NULL,NULL,'20018',NULL,'Hortalizas Nacionales','1 kg'),(2164,'Cebolla Blanca',NULL,2,10.00,120,NULL,NULL,'20019',NULL,'Hortalizas del Campo','1 kg'),(2165,'Lechuga Romana',NULL,2,8.00,150,NULL,NULL,'20020',NULL,'Hortalizas Frescas','1 unidad'),(2166,'Espinaca Fresca',NULL,2,12.00,100,NULL,NULL,'20021',NULL,'Hortalizas del Campo','250 g'),(2167,'Repollo Verde',NULL,2,10.00,80,NULL,NULL,'20022',NULL,'Hortalizas Nacionales','1 kg'),(2168,'Papa Blanca',NULL,2,12.00,200,NULL,NULL,'20023',NULL,'Papas del Norte','1 kg'),(2169,'Camote Naranja',NULL,2,15.00,120,NULL,NULL,'20024',NULL,'Hortalizas Frescas','1 kg'),(2170,'Acelga Fresca',NULL,2,10.00,100,NULL,NULL,'20025',NULL,'Hortalizas del Campo','250 g'),(2171,'Brócoli Fresco',NULL,2,18.00,80,NULL,NULL,'20026',NULL,'Hortalizas Nacionales','1 unidad'),(2172,'Coliflor Blanca',NULL,2,20.00,90,NULL,NULL,'20027',NULL,'Hortalizas Frescas','1 unidad'),(2173,'Judía Verde',NULL,2,15.00,120,NULL,NULL,'20028',NULL,'Hortalizas del Campo','500 g'),(2174,'Chayote Fresco',NULL,2,10.00,100,NULL,NULL,'20029',NULL,'Hortalizas Nacionales','1 kg'),(2175,'Haba Tierna',NULL,2,22.30,80,NULL,NULL,'20030',NULL,'Hortalizas del Campo','1 kg'),(2176,'Pan Blanco',NULL,3,15.00,40,NULL,NULL,'30001',NULL,'La Central','1 unidad'),(2177,'Bollos de Leche',NULL,3,18.00,30,NULL,NULL,'30002',NULL,'Panadería San José','6 unidades'),(2178,'Conchas',NULL,3,10.00,35,NULL,NULL,'30003',NULL,'Panadería Santa María','1 unidad'),(2179,'Rosca de Reyes',NULL,3,50.00,25,NULL,NULL,'30004',NULL,'Panadería Las Delicias','1 unidad'),(2180,'Croissants',NULL,3,20.00,30,NULL,NULL,'30005',NULL,'La Francia','4 unidades'),(2181,'Pan de Nuez',NULL,3,25.00,20,NULL,NULL,'30006',NULL,'Panadería Nueces y Miel','1 unidad'),(2182,'Bollos de Canela',NULL,3,22.00,25,NULL,NULL,'30007',NULL,'Panadería Canela y Azúcar','6 unidades'),(2183,'Baguette',NULL,3,12.00,40,NULL,NULL,'30008',NULL,'Panadería Parisienne','1 unidad'),(2184,'Pan Integral',NULL,3,18.00,35,NULL,NULL,'30009',NULL,'La Central','1 unidad'),(2185,'Pan de Queso',NULL,3,20.00,30,NULL,NULL,'30010',NULL,'Panadería Queso y Mantequilla','1 unidad'),(2186,'Pan de Chocolate',NULL,3,22.00,25,NULL,NULL,'30011',NULL,'La Francia','1 unidad'),(2187,'Empanadas de Pollo',NULL,3,30.00,20,NULL,NULL,'30012',NULL,'Panadería Las Empanadas','6 unidades'),(2188,'Pan de Centeno',NULL,3,16.00,30,NULL,NULL,'30013',NULL,'Panadería Centeno Fresco','1 unidad'),(2189,'Pan de Avena',NULL,3,18.00,35,NULL,NULL,'30014',NULL,'Panadería Avena y Miel','1 unidad'),(2190,'Pan de Pasas',NULL,3,22.00,25,NULL,NULL,'30015',NULL,'La Francia','1 unidad'),(2191,'Conchas Rellenas de Crema',NULL,3,20.00,30,NULL,NULL,'30016',NULL,'Panadería Santa María','4 unidades'),(2192,'Pan de Centeno Integral',NULL,3,16.00,30,NULL,NULL,'30017',NULL,'Panadería Centeno Fresco','1 unidad'),(2193,'Pan de Nuez y Pasas',NULL,3,24.00,25,NULL,NULL,'30018',NULL,'Panadería Nueces y Miel','1 unidad'),(2194,'Pan de Elote',NULL,3,20.00,20,NULL,NULL,'30019',NULL,'Panadería Maíz y Dulzura','1 unidad'),(2195,'Bollos de Vainilla',NULL,3,18.00,30,NULL,NULL,'30020',NULL,'Panadería Vainilla y Azúcar','6 unidades'),(2196,'Galletas de Avena',NULL,4,20.00,50,NULL,NULL,'40001',NULL,'Quaker','Paquete de 300g'),(2197,'Galletas de Chocolate',NULL,4,25.00,40,NULL,NULL,'40002',NULL,'Chips Ahoy!','Paquete de 350g'),(2198,'Galletas de Vainilla',NULL,4,18.00,45,NULL,NULL,'40003',NULL,'María','Paquete de 400g'),(2199,'Galletas de Mantequilla',NULL,4,15.00,35,NULL,NULL,'40004',NULL,'Keebler','Paquete de 250g'),(2200,'Galletas de Chocolate y Nueces',NULL,4,22.00,30,NULL,NULL,'40005',NULL,'Pepperidge Farm','Paquete de 300g'),(2201,'Cereal de Maíz',NULL,4,28.00,40,NULL,NULL,'40006',NULL,'Kellogg\'s','Caja de 500g'),(2202,'Cereal de Trigo',NULL,4,26.00,35,NULL,NULL,'40007',NULL,'Post','Caja de 450g'),(2203,'Cereal de Avena',NULL,4,30.00,45,NULL,NULL,'40008',NULL,'Quaker','Caja de 400g'),(2204,'Cereal de Arroz Inflado',NULL,4,24.00,50,NULL,NULL,'40009',NULL,'Kellogg\'s','Caja de 350g'),(2205,'Cereal de Miel y Almendras',NULL,4,32.00,40,NULL,NULL,'40010',NULL,'Nature Valley','Caja de 400g'),(2206,'Cereal de Frutas',NULL,4,26.00,45,NULL,NULL,'40011',NULL,'Special K','Caja de 350g'),(2207,'Galletas Integrales',NULL,4,18.00,30,NULL,NULL,'40012',NULL,'Fiber One','Paquete de 300g'),(2208,'Galletas de Coco',NULL,4,16.00,35,NULL,NULL,'40013',NULL,'Keebler','Paquete de 250g'),(2209,'Galletas de Avena y Pasas',NULL,4,20.00,40,NULL,NULL,'40014',NULL,'Quaker','Paquete de 300g'),(2210,'Galletas de Chispas de Chocolate',NULL,4,24.00,45,NULL,NULL,'40015',NULL,'Chips Deluxe','Paquete de 350g'),(2211,'Cereal de Granola',NULL,4,28.00,35,NULL,NULL,'40016',NULL,'Bear Naked','Caja de 400g'),(2212,'Cereal de Salvado',NULL,4,26.00,40,NULL,NULL,'40017',NULL,'Post','Caja de 450g'),(2213,'Cereal de Maíz con Miel',NULL,4,30.00,50,NULL,NULL,'40018',NULL,'Kellogg\'s','Caja de 500g'),(2214,'Cereal de Arroz con Chocolate',NULL,4,25.00,40,NULL,NULL,'40019',NULL,'Cocoa Pebbles','Caja de 350g'),(2215,'Cereal de Trigo Integral',NULL,4,28.00,45,NULL,NULL,'40020',NULL,'Fiber One','Caja de 400g'),(2216,'Galletas Rellenas de Crema',NULL,4,22.00,30,NULL,NULL,'40021',NULL,'Oreo','Paquete de 350g'),(2217,'Galletas de Miel',NULL,4,18.00,35,NULL,NULL,'40022',NULL,'Nature Valley','Paquete de 300g'),(2218,'Galletas de Mantequilla de Maní',NULL,4,16.00,40,NULL,NULL,'40023',NULL,'Nutter Butter','Paquete de 250g'),(2219,'Galletas de Canela',NULL,4,24.00,45,NULL,NULL,'40024',NULL,'Cinnamon Toast Crunch','Paquete de 350g'),(2220,'Cereal de Hojuelas de Maíz',NULL,4,26.00,35,NULL,NULL,'40025',NULL,'Corn Flakes','Caja de 400g'),(2221,'Cereal de Hojuelas de Avena',NULL,4,28.00,40,NULL,NULL,'40026',NULL,'Quaker','Caja de 450g'),(2222,'Cereal de Hojuelas de Trigo',NULL,4,30.00,50,NULL,NULL,'40027',NULL,'Kellogg\'s','Caja de 500g'),(2223,'Cereal de Hojuelas de Arroz',NULL,4,25.00,40,NULL,NULL,'40028',NULL,'Rice Krispies','Caja de 350g'),(2224,'Galletas de Jengibre',NULL,4,20.00,45,NULL,NULL,'40029',NULL,'Ginger Snaps','Paquete de 300g'),(2225,'Galletas de Limón',NULL,4,18.00,30,NULL,NULL,'40030',NULL,'Lemon Coolers','Paquete de 250g'),(2226,'Galletas de Chocolate Blanco',NULL,4,24.00,35,NULL,NULL,'40031',NULL,'White Chocolate Macadamia','Paquete de 350g'),(2227,'Cereal de Hojuelas de Maíz con Azúcar',NULL,4,26.00,45,NULL,NULL,'40032',NULL,'Frosted Flakes','Caja de 400g'),(2228,'Cereal de Maíz con Miel y Almendras',NULL,4,30.00,50,NULL,NULL,'40033',NULL,'Honey Bunches of Oats','Caja de 450g'),(2229,'Cereal de Maíz con Chocolate',NULL,4,28.00,40,NULL,NULL,'40034',NULL,'Chocolate Frosted Flakes','Caja de 500g'),(2230,'Cereal de Arroz con Miel',NULL,4,25.00,45,NULL,NULL,'40035',NULL,'Honey Smacks','Caja de 350g'),(2231,'Cereal de Arroz con Chocolate y Malvaviscos',NULL,4,26.00,35,NULL,NULL,'40036',NULL,'Cocoa Krispies','Caja de 400g'),(2232,'Cereal de Trigo con Miel',NULL,4,28.00,40,NULL,NULL,'40037',NULL,'Honey Nut Cheerios','Caja de 450g'),(2233,'Cereal de Trigo con Miel y Almendras',NULL,4,32.00,50,NULL,NULL,'40038',NULL,'Honey Bunches of Oats with Almonds','Caja de 500g'),(2234,'Cereal de Trigo con Pasas',NULL,4,26.00,40,NULL,NULL,'40039',NULL,'Raisin Bran','Caja de 400g'),(2235,'Cereal de Trigo con Chocolate',NULL,4,28.00,45,NULL,NULL,'40040',NULL,'Chocolate Cheerios','Caja de 450g'),(2236,'Galletas de Avena y Chocolate',NULL,4,22.00,30,NULL,NULL,'40041',NULL,'Quaker','Paquete de 350g'),(2237,'Galletas de Vainilla y Limón',NULL,4,20.00,35,NULL,NULL,'40042',NULL,'Vanilla Lemon Wafers','Paquete de 300g'),(2238,'Galletas de Naranja y Jengibre',NULL,4,18.00,40,NULL,NULL,'40043',NULL,'Orange Ginger Snaps','Paquete de 250g'),(2239,'Galletas de Avena y Pasas',NULL,4,24.00,45,NULL,NULL,'40044',NULL,'Raisin Oatmeal Cookies','Paquete de 350g'),(2240,'Cereal de Hojuelas de Avena con Pasas',NULL,4,26.00,35,NULL,NULL,'40045',NULL,'Quaker','Caja de 400g'),(2241,'Cereal de Hojuelas de Trigo con Frutas',NULL,4,28.00,40,NULL,NULL,'40046',NULL,'Kellogg\'s','Caja de 450g'),(2242,'Cereal de Hojuelas de Arroz con Miel y Almendras',NULL,4,30.00,50,NULL,NULL,'40047',NULL,'Honey Bunches of Oats with Almonds','Caja de 500g'),(2243,'Cereal de Maíz con Miel y Nueces',NULL,4,26.00,40,NULL,NULL,'40048',NULL,'Honey Bunches of Oats with Pecan Bunches','Caja de 400g'),(2244,'Cereal de Arroz Inflado con Chocolate',NULL,4,28.00,45,NULL,NULL,'40049',NULL,'Cocoa Puffs','Caja de 450g'),(2245,'Cereal de Trigo con Miel y Nueces',NULL,4,30.00,50,NULL,NULL,'40050',NULL,'Honey Nut Cheerios','Caja de 500g'),(2246,'Refresco de Cola',NULL,5,15.00,100,NULL,NULL,'50001',NULL,'Coca-Cola','Botella de 2 litros'),(2247,'Agua Mineral',NULL,5,10.00,120,NULL,NULL,'50002',NULL,'Ciel','Botella de 1.5 litros'),(2248,'Jugo de Naranja',NULL,5,20.00,80,NULL,NULL,'50003',NULL,'Del Valle','Botella de 1 litro'),(2249,'Té Verde',NULL,5,18.00,60,NULL,NULL,'50004',NULL,'Lipton','Botella de 500ml'),(2250,'Cerveza Lager',NULL,5,22.00,50,NULL,NULL,'50005',NULL,'Corona','Lata de 355ml'),(2251,'Vino Tinto',NULL,5,35.00,40,NULL,NULL,'50006',NULL,'Casillero del Diablo','Botella de 750ml'),(2252,'Whisky Escocés',NULL,5,50.00,30,NULL,NULL,'50007',NULL,'Johnnie Walker','Botella de 750ml'),(2253,'Ron Dorado',NULL,5,40.00,35,NULL,NULL,'50008',NULL,'Havana Club','Botella de 750ml'),(2254,'Tequila Reposado',NULL,5,45.00,25,NULL,NULL,'50009',NULL,'José Cuervo','Botella de 750ml'),(2255,'Refresco de Limón',NULL,5,15.00,90,NULL,NULL,'50010',NULL,'Sprite','Botella de 2 litros'),(2256,'Agua Mineral con Gas',NULL,5,12.00,80,NULL,NULL,'50011',NULL,'Perrier','Botella de 750ml'),(2257,'Jugo de Manzana',NULL,5,20.00,70,NULL,NULL,'50012',NULL,'Del Valle','Botella de 1 litro'),(2258,'Té Negro',NULL,5,18.00,55,NULL,NULL,'50013',NULL,'Twinings','Botella de 500ml'),(2259,'Cerveza IPA',NULL,5,25.00,45,NULL,NULL,'50014',NULL,'Heineken','Lata de 355ml'),(2260,'Vino Blanco',NULL,5,30.00,35,NULL,NULL,'50015',NULL,'Santa Carolina','Botella de 750ml'),(2261,'Vodka',NULL,5,40.00,40,NULL,NULL,'50016',NULL,'Absolut','Botella de 750ml'),(2262,'Ginebra',NULL,5,35.00,30,NULL,NULL,'50017',NULL,'Bombay Sapphire','Botella de 750ml'),(2263,'Whisky Bourbon',NULL,5,45.00,25,NULL,NULL,'50018',NULL,'Jack Daniel\'s','Botella de 750ml'),(2264,'Tequila Blanco',NULL,5,50.00,20,NULL,NULL,'50019',NULL,'Patrón','Botella de 750ml'),(2265,'Refresco de Toronja',NULL,5,15.00,85,NULL,NULL,'50020',NULL,'Fanta','Botella de 2 litros'),(2266,'Agua Mineral Saborizada',NULL,5,10.00,75,NULL,NULL,'50021',NULL,'Dasani','Botella de 500ml'),(2267,'Jugo de Uva',NULL,5,20.00,65,NULL,NULL,'50022',NULL,'Del Valle','Botella de 1 litro'),(2268,'Té de Hierbas',NULL,5,18.00,50,NULL,NULL,'50023',NULL,'Tazo','Botella de 500ml'),(2269,'Cerveza Stout',NULL,5,28.00,40,NULL,NULL,'50024',NULL,'Guinness','Lata de 355ml'),(2270,'Vino Rosado',NULL,5,25.00,30,NULL,NULL,'50025',NULL,'Cono Sur','Botella de 750ml'),(2271,'Coñac',NULL,5,50.00,35,NULL,NULL,'50026',NULL,'Hennessy','Botella de 750ml'),(2272,'Licor de Café',NULL,5,40.00,25,NULL,NULL,'50027',NULL,'Kahlúa','Botella de 750ml'),(2273,'Rum Blanco',NULL,5,45.00,30,NULL,NULL,'50028',NULL,'Bacardi','Botella de 750ml'),(2274,'Mezcal',NULL,5,55.00,20,NULL,NULL,'50029',NULL,'Montelobos','Botella de 750ml'),(2275,'Refresco de Manzana',NULL,5,15.00,80,NULL,NULL,'50030',NULL,'Sidral Mundet','Botella de 2 litros'),(2276,'Agua de Coco',NULL,5,12.00,70,NULL,NULL,'50031',NULL,'Vita Coco','Botella de 500ml'),(2277,'Jugo de Piña',NULL,5,20.00,60,NULL,NULL,'50032',NULL,'Del Valle','Botella de 1 litro'),(2278,'Té Oolong',NULL,5,18.00,45,NULL,NULL,'50033',NULL,'Foojoy','Botella de 500ml'),(2279,'Cerveza Pale Ale',NULL,5,26.00,35,NULL,NULL,'50034',NULL,'Sierra Nevada','Lata de 355ml'),(2280,'Detergente Líquido para Ropa',NULL,6,35.00,50,NULL,NULL,'60001',NULL,'Ariel','1 litro'),(2281,'Limpiador Multiusos',NULL,6,25.00,40,NULL,NULL,'60002',NULL,'Clorox','1 litro'),(2282,'Desinfectante de Superficies',NULL,6,30.00,45,NULL,NULL,'60003',NULL,'Lysol','500 ml'),(2283,'Jabón para Trastes',NULL,6,20.00,35,NULL,NULL,'60004',NULL,'Dawn','750 ml'),(2284,'Limpiavidrios',NULL,6,18.00,40,NULL,NULL,'60005',NULL,'Windex','500 ml'),(2285,'Desengrasante',NULL,6,22.00,30,NULL,NULL,'60006',NULL,'Mr. Muscle','750 ml'),(2286,'Quitamanchas',NULL,6,28.00,25,NULL,NULL,'60007',NULL,'Vanish','500 ml'),(2287,'Blanqueador',NULL,6,15.00,45,NULL,NULL,'60008',NULL,'Cloralex','1 litro'),(2288,'Suavizante de Telas',NULL,6,30.00,50,NULL,NULL,'60009',NULL,'Downy','1 litro'),(2289,'Toallas de Papel',NULL,6,12.00,55,NULL,NULL,'60010',NULL,'Scott','Rollo de 2 paquetes'),(2290,'Servilletas de Papel',NULL,6,8.00,60,NULL,NULL,'60011',NULL,'Viva','Paquete de 100 unidades'),(2291,'Bolsas de Basura',NULL,6,20.00,40,NULL,NULL,'60012',NULL,'Hefty','Rollo de 30 bolsas'),(2292,'Esponjas para Limpieza',NULL,6,10.00,50,NULL,NULL,'60013',NULL,'Scotch-Brite','Paquete de 5 unidades'),(2293,'Trapo de Microfibra',NULL,6,6.00,45,NULL,NULL,'60014',NULL,'Quickie','1 unidad'),(2294,'Cepillo para Inodoro',NULL,6,8.00,30,NULL,NULL,'60015',NULL,'Scotch-Brite','1 unidad'),(2295,'Escoba',NULL,6,18.00,35,NULL,NULL,'60016',NULL,'Swiffer','1 unidad'),(2296,'Recogedor',NULL,6,10.00,40,NULL,NULL,'60017',NULL,'Libman','1 unidad'),(2297,'Plumero',NULL,6,12.00,45,NULL,NULL,'60018',NULL,'O-Cedar','1 unidad'),(2298,'Mopa',NULL,6,25.00,50,NULL,NULL,'60019',NULL,'Rubbermaid','1 unidad'),(2299,'Desodorante de Ambiente',NULL,6,15.00,55,NULL,NULL,'60020',NULL,'Glade','250 ml'),(2300,'Desinfectante de Manos',NULL,6,10.00,60,NULL,NULL,'60021',NULL,'Purell','250 ml'),(2301,'Limpiador de Baños',NULL,6,20.00,45,NULL,NULL,'60022',NULL,'Tilex','500 ml'),(2302,'Limpiador de Cocina',NULL,6,18.00,40,NULL,NULL,'60023',NULL,'Easy-Off','750 ml'),(2303,'Limpiador de Pisos',NULL,6,20.00,50,NULL,NULL,'60024',NULL,'Pine-Sol','1 litro'),(2304,'Limpiador de Muebles',NULL,6,22.00,35,NULL,NULL,'60025',NULL,'Old English','500 ml'),(2305,'Desatascador de Cañerías',NULL,6,25.00,30,NULL,NULL,'60026',NULL,'Liquid Plumr','500 ml'),(2306,'Pastillas para Lavavajillas',NULL,6,30.00,55,NULL,NULL,'60027',NULL,'Finish','Paquete de 20 unidades'),(2307,'Aromatizante de Telas',NULL,6,15.00,40,NULL,NULL,'60028',NULL,'Febreze','250 ml'),(2308,'Desengrasante para Cocina',NULL,6,20.00,35,NULL,NULL,'60029',NULL,'Easy-Off','500 ml'),(2309,'Limpiador de Vidrios y Espejos',NULL,6,18.00,40,NULL,NULL,'60030',NULL,'Windex','500 ml'),(2310,'Limpiador de Superficies de Acero Inoxidable',NULL,6,22.00,45,NULL,NULL,'60031',NULL,'Weiman','500 ml'),(2311,'Desinfectante de Juguetes',NULL,6,12.00,30,NULL,NULL,'60032',NULL,'Clorox','250 ml'),(2312,'Limpia Alfombras',NULL,6,20.00,35,NULL,NULL,'60033',NULL,'Resolve','500 ml'),(2313,'Removedor de Manchas de Tela',NULL,6,18.00,40,NULL,NULL,'60034',NULL,'Shout','500 ml'),(2314,'Desinfectante de Zapatos',NULL,6,15.00,45,NULL,NULL,'60035',NULL,'Lysol','250 ml'),(2315,'Limpiador de Azulejos',NULL,6,20.00,50,NULL,NULL,'60036',NULL,'Tilex','500 ml'),(2316,'Limpiador de Acero Inoxidable',NULL,6,25.00,55,NULL,NULL,'60037',NULL,'Bar Keepers Friend','500 ml'),(2317,'Limpiador de Pisos Laminados',NULL,6,18.00,40,NULL,NULL,'60038',NULL,'Bona','750 ml'),(2318,'Limpiador de Alfombras',NULL,6,20.00,45,NULL,NULL,'60039',NULL,'Bissell','500 ml'),(2319,'Removedor de Olores para Mascotas',NULL,6,15.00,30,NULL,NULL,'60040',NULL,'Nature\'s Miracle','500 ml'),(2320,'Limpiador de Desagües',NULL,6,22.00,35,NULL,NULL,'60041',NULL,'Drano','500 ml'),(2321,'Blanqueador de Juntas',NULL,6,18.00,40,NULL,NULL,'60042',NULL,'Clorox','500 ml'),(2322,'Limpiador de Hornos',NULL,6,25.00,45,NULL,NULL,'60043',NULL,'Easy-Off','500 ml'),(2323,'Desinfectante de Alfombras',NULL,6,20.00,50,NULL,NULL,'60044',NULL,'Lysol','500 ml'),(2324,'Limpiador de Persianas',NULL,6,15.00,55,NULL,NULL,'60045',NULL,'Swiffer','500 ml'),(2325,'Limpiador de Cuero',NULL,6,25.00,30,NULL,NULL,'60046',NULL,'Lexol','500 ml'),(2326,'Removedor de Olores para Ropa',NULL,6,18.00,35,NULL,NULL,'60047',NULL,'Febreze','500 ml'),(2327,'Limpiador de Computadoras',NULL,6,22.00,40,NULL,NULL,'60048',NULL,'Falcon','500 ml'),(2328,'Limpiador de Zapatos',NULL,6,20.00,45,NULL,NULL,'60049',NULL,'Kiwi','250 ml'),(2329,'Limpiador de Paredes',NULL,6,25.00,50,NULL,NULL,'60050',NULL,'Magic Eraser','500 ml'),(2330,'Papas Fritas Clásicas',NULL,7,25.00,50,NULL,NULL,'70001',NULL,'Lay\'s','Bolsa de 200g'),(2331,'Doritos Nacho',NULL,7,28.00,45,NULL,NULL,'70002',NULL,'Doritos','Bolsa de 220g'),(2332,'Cheetos Crunchy',NULL,7,30.00,40,NULL,NULL,'70003',NULL,'Cheetos','Bolsa de 250g'),(2333,'Papas Fritas Sabor Jalapeño',NULL,7,22.00,35,NULL,NULL,'70004',NULL,'Sabritas','Bolsa de 180g'),(2334,'Fritos',NULL,7,24.00,30,NULL,NULL,'70005',NULL,'Fritos','Bolsa de 200g'),(2335,'Papas Fritas Onduladas',NULL,7,27.00,35,NULL,NULL,'70006',NULL,'Ruffles','Bolsa de 220g'),(2336,'Cacahuates Enchilados',NULL,7,35.00,40,NULL,NULL,'70007',NULL,'Manzanita Sol','Bolsa de 300g'),(2337,'Tostitos Salsa Picante',NULL,7,30.00,45,NULL,NULL,'70008',NULL,'Tostitos','Bolsa de 250g'),(2338,'Papas Fritas de Camote',NULL,7,32.00,50,NULL,NULL,'70009',NULL,'Terra','Bolsa de 250g'),(2339,'Doritos Flamin\' Hot',NULL,7,29.00,40,NULL,NULL,'70010',NULL,'Doritos','Bolsa de 240g'),(2340,'Cheetos Puffs',NULL,7,33.00,35,NULL,NULL,'70011',NULL,'Cheetos','Bolsa de 280g'),(2341,'Papas Fritas con Sabor a Barbacoa',NULL,7,28.00,45,NULL,NULL,'70012',NULL,'Lay\'s','Bolsa de 220g'),(2342,'Doritos Cool Ranch',NULL,7,27.00,30,NULL,NULL,'70013',NULL,'Doritos','Bolsa de 230g'),(2343,'Cacahuates Salados',NULL,7,35.00,35,NULL,NULL,'70014',NULL,'Manzanita Sol','Bolsa de 300g'),(2344,'Papas Fritas de Calabaza',NULL,7,25.00,40,NULL,NULL,'70015',NULL,'Terra','Bolsa de 200g'),(2345,'Takis Fuego',NULL,7,29.00,45,NULL,NULL,'70016',NULL,'Barcel','Bolsa de 240g'),(2346,'Cheetos Mix',NULL,7,32.00,50,NULL,NULL,'70017',NULL,'Cheetos','Bolsa de 300g'),(2347,'Papas Fritas Sabor Limón',NULL,7,28.00,40,NULL,NULL,'70018',NULL,'Lay\'s','Bolsa de 220g'),(2348,'Doritos Salsa Verde',NULL,7,27.00,35,NULL,NULL,'70019',NULL,'Doritos','Bolsa de 230g'),(2349,'Cacahuates Japoneses',NULL,7,33.00,30,NULL,NULL,'70020',NULL,'Manzanita Sol','Bolsa de 280g'),(2350,'Papas Fritas de Plátano',NULL,7,32.00,35,NULL,NULL,'70021',NULL,'Terra','Bolsa de 250g'),(2351,'Tostitos Queso',NULL,7,31.00,40,NULL,NULL,'70022',NULL,'Tostitos','Bolsa de 260g'),(2352,'Cheetos Pelotazos',NULL,7,33.00,45,NULL,NULL,'70023',NULL,'Cheetos','Bolsa de 280g'),(2353,'Papas Fritas Sabor a Queso',NULL,7,30.00,50,NULL,NULL,'70024',NULL,'Lay\'s','Bolsa de 240g'),(2354,'Doritos Dinamita',NULL,7,29.00,40,NULL,NULL,'70025',NULL,'Doritos','Bolsa de 250g'),(2355,'Cacahuates con Chile y Limón',NULL,7,35.00,35,NULL,NULL,'70026',NULL,'Manzanita Sol','Bolsa de 300g'),(2356,'Papas Fritas de Remolacha',NULL,7,28.00,30,NULL,NULL,'70027',NULL,'Terra','Bolsa de 220g'),(2357,'Takis Nitro',NULL,7,30.00,35,NULL,NULL,'70028',NULL,'Barcel','Bolsa de 240g'),(2358,'Cheetos Bolitas',NULL,7,33.00,40,NULL,NULL,'70029',NULL,'Cheetos','Bolsa de 280g'),(2359,'Papas Fritas Sabor a Chile',NULL,7,30.00,45,NULL,NULL,'70030',NULL,'Lay\'s','Bolsa de 240g'),(2360,'Doritos Pizzerolas',NULL,7,28.00,30,NULL,NULL,'70031',NULL,'Doritos','Bolsa de 230g'),(2361,'Cacahuates con Chile',NULL,7,33.00,35,NULL,NULL,'70032',NULL,'Manzanita Sol','Bolsa de 280g'),(2362,'Papas Fritas de Yuca',NULL,7,32.00,40,NULL,NULL,'70033',NULL,'Terra','Bolsa de 250g'),(2363,'Tostitos Salsa de Queso',NULL,7,31.00,45,NULL,NULL,'70034',NULL,'Tostitos','Bolsa de 260g'),(2364,'Cheetos Torciditos',NULL,7,32.00,50,NULL,NULL,'70035',NULL,'Cheetos','Bolsa de 300g'),(2365,'Papas Fritas con Sabor a Queso y Chile',NULL,7,31.00,55,NULL,NULL,'70036',NULL,'Lay\'s','Bolsa de 260g'),(2366,'Takis Fuego Extra',NULL,7,30.00,60,NULL,NULL,'70037',NULL,'Barcel','Bolsa de 250g'),(2367,'Doritos Salsa Brava',NULL,7,28.00,65,NULL,NULL,'70038',NULL,'Doritos','Bolsa de 230g'),(2368,'Cacahuates con Chile y Limón y Sal',NULL,7,33.00,70,NULL,NULL,'70039',NULL,'Manzanita Sol','Bolsa de 280g'),(2369,'Papas Fritas de Remolacha y Zanahoria',NULL,7,32.00,75,NULL,NULL,'70040',NULL,'Terra','Bolsa de 250g'),(2370,'Tostitos Salsa de Queso Extra',NULL,7,31.00,80,NULL,NULL,'70041',NULL,'Tostitos','Bolsa de 260g'),(2371,'Cheetos Torciditos Flamin\' Hot',NULL,7,32.00,85,NULL,NULL,'70042',NULL,'Cheetos','Bolsa de 300g'),(2372,'Doritos Dinamita Salsa Brava',NULL,7,29.00,90,NULL,NULL,'70043',NULL,'Doritos','Bolsa de 250g'),(2373,'Cacahuates con Chile Habanero',NULL,7,33.00,95,NULL,NULL,'70044',NULL,'Manzanita Sol','Bolsa de 280g'),(2374,'Papas Fritas con Sabor a Queso y Chile Picante',NULL,7,31.00,100,NULL,NULL,'70045',NULL,'Lay\'s','Bolsa de 260g'),(2375,'Takis Fuego Extreme',NULL,7,30.00,105,NULL,NULL,'70046',NULL,'Barcel','Bolsa de 250g'),(2376,'Doritos Salsa Brava con Limón',NULL,7,28.00,110,NULL,NULL,'70047',NULL,'Doritos','Bolsa de 230g'),(2377,'Cacahuates con Chile y Limón y Sal Picante',NULL,7,33.00,115,NULL,NULL,'70048',NULL,'Manzanita Sol','Bolsa de 280g'),(2378,'Papas Fritas de Remolacha y Zanahoria Extra',NULL,7,32.00,120,NULL,NULL,'70049',NULL,'Terra','Bolsa de 250g'),(2379,'Tostitos Salsa de Queso Extra Picante',NULL,7,31.00,125,NULL,NULL,'70050',NULL,'Tostitos','Bolsa de 260g'),(2380,'Shampoo Anti-Caspa',NULL,8,60.00,60,NULL,NULL,'80001',NULL,'Head & Shoulders','Botella de 400ml'),(2381,'Acondicionador Reparador',NULL,8,65.00,55,NULL,NULL,'80002',NULL,'Pantene','Botella de 350ml'),(2382,'Jabón de Tocador',NULL,8,55.00,70,NULL,NULL,'80003',NULL,'Dove','Paquete de 4 unidades'),(2383,'Desodorante Roll-On',NULL,8,75.00,65,NULL,NULL,'80004',NULL,'Rexona','Frascos de 50ml'),(2384,'Crema Facial Hidratante',NULL,8,85.00,50,NULL,NULL,'80005',NULL,'Neutrogena','Tubo de 75g'),(2385,'Cepillo de Dientes Eléctrico',NULL,8,165.00,40,NULL,NULL,'80006',NULL,'Oral-B','Incluye 2 cabezales'),(2386,'Enjuague Bucal',NULL,8,75.00,55,NULL,NULL,'80007',NULL,'Listerine','Botella de 500ml'),(2387,'Cera Capilar Mate',NULL,8,80.00,50,NULL,NULL,'80008',NULL,'L\'Oréal','Frasco de 100g'),(2388,'Loción Astringente',NULL,8,70.00,45,NULL,NULL,'80009',NULL,'Clean & Clear','Botella de 200ml'),(2389,'Gel de Baño Hidratante',NULL,8,60.00,60,NULL,NULL,'80010',NULL,'Nivea','Botella de 500ml'),(2390,'Desodorante en Spray',NULL,8,70.00,70,NULL,NULL,'80011',NULL,'Axe','Aerosol de 150ml'),(2391,'Mascarilla Capilar Nutritiva',NULL,8,55.00,50,NULL,NULL,'80012',NULL,'Garnier Fructis','Sobre de 200ml'),(2392,'Toallas Desmaquillantes',NULL,8,65.00,65,NULL,NULL,'80013',NULL,'Olay','Paquete de 30 unidades'),(2393,'Gel de Afeitar',NULL,8,70.00,60,NULL,NULL,'80014',NULL,'Gillette','Botella de 200ml'),(2394,'Crema Hidratante Corporal',NULL,8,80.00,55,NULL,NULL,'80015',NULL,'Aveeno','Botella de 400ml'),(2395,'Cepillo de Pelo',NULL,8,95.00,45,NULL,NULL,'80016',NULL,'Tangle Teezer','Unidad'),(2396,'Delineador de Ojos',NULL,8,90.00,50,NULL,NULL,'80017',NULL,'Maybelline','Lápiz de 1.5g'),(2397,'Perfume de Mujer',NULL,8,105.00,40,NULL,NULL,'80018',NULL,'Calvin Klein','Frasco de 50ml'),(2398,'Perfume de Hombre',NULL,8,115.00,35,NULL,NULL,'80019',NULL,'Hugo Boss','Frasco de 100ml'),(2399,'Esmalte de Uñas',NULL,8,80.00,60,NULL,NULL,'80020',NULL,'Essie','Frasco de 13.5ml'),(2400,'Maquillaje Base',NULL,8,85.00,55,NULL,NULL,'80021',NULL,'L\'Oréal Paris','Frasco de 30ml'),(2401,'Desodorante en Barra',NULL,8,65.00,70,NULL,NULL,'80022',NULL,'Dove','Envase de 75g'),(2402,'Gel de Ducha Exfoliante',NULL,8,60.00,60,NULL,NULL,'80023',NULL,'Nivea Men','Botella de 400ml'),(2403,'Máscara de Pestañas',NULL,8,95.00,50,NULL,NULL,'80024',NULL,'Maybelline','Tubo de 9.5ml'),(2404,'Crema para Manos',NULL,8,75.00,65,NULL,NULL,'80025',NULL,'Neutrogena','Tubo de 50ml'),(2405,'Bálsamo Labial',NULL,8,65.00,60,NULL,NULL,'80026',NULL,'Burt\'s Bees','Tubo de 4.25g'),(2406,'Shampoo Voluminizador',NULL,8,75.00,55,NULL,NULL,'80027',NULL,'TRESemmé','Botella de 500ml'),(2407,'Máscara Capilar Reparadora',NULL,8,70.00,50,NULL,NULL,'80028',NULL,'Dove','Tubo de 200ml'),(2408,'Desodorante en Crema',NULL,8,65.00,60,NULL,NULL,'80029',NULL,'Nivea','Frascos de 50g'),(2409,'Gel Limpiador Facial',NULL,8,80.00,65,NULL,NULL,'80030',NULL,'Cetaphil','Botella de 200ml'),(2410,'Cera para Depilación',NULL,8,70.00,45,NULL,NULL,'80031',NULL,'Veet','Frasco de 100ml'),(2411,'Loción Post-Afeitado',NULL,8,65.00,55,NULL,NULL,'80032',NULL,'Nivea Men','Botella de 100ml'),(2412,'Toallas Íntimas',NULL,8,60.00,70,NULL,NULL,'80033',NULL,'Always','Paquete de 30 unidades'),(2413,'Bálsamo para Barba',NULL,8,80.00,60,NULL,NULL,'80034',NULL,'Beardbrand','Frasco de 50ml'),(2414,'Crema Antimanchas',NULL,8,95.00,40,NULL,NULL,'80035',NULL,'La Roche-Posay','Tubo de 30ml'),(2415,'Gel de Baño Energizante',NULL,8,70.00,55,NULL,NULL,'80036',NULL,'Yves Rocher','Botella de 400ml'),(2416,'Desodorante Antitranspirante',NULL,8,80.00,50,NULL,NULL,'80037',NULL,'Mitchum','Frascos de 150ml'),(2417,'Crema Antiedad',NULL,8,110.00,45,NULL,NULL,'80038',NULL,'Olay','Tubo de 50ml'),(2418,'Afeitadora Desechable',NULL,8,75.00,60,NULL,NULL,'80039',NULL,'Gillette Venus','Paquete de 5 unidades'),(2419,'Colonia Infantil',NULL,8,50.00,70,NULL,NULL,'80040',NULL,'Johnson\'s Baby','Frasco de 100ml'),(2420,'Gel Fijador para Cabello',NULL,8,65.00,55,NULL,NULL,'80041',NULL,'Garnier Fructis','Frasco de 200ml'),(2421,'Cepillo de Dientes',NULL,8,25.00,60,NULL,NULL,'80042',NULL,'Colgate','Unidad'),(2422,'Tónico Facial',NULL,8,70.00,45,NULL,NULL,'80043',NULL,'Thayers','Botella de 355ml'),(2423,'Acondicionador Sin Enjuague',NULL,8,60.00,50,NULL,NULL,'80044',NULL,'Herbal Essences','Botella de 250ml'),(2424,'Crema para Pies',NULL,8,75.00,55,NULL,NULL,'80045',NULL,'Scholl','Tubo de 100ml'),(2425,'Gel de Baño Relajante',NULL,8,70.00,60,NULL,NULL,'80046',NULL,'Dove','Botella de 500ml'),(2426,'Desodorante en Spray Fresco',NULL,8,70.00,65,NULL,NULL,'80047',NULL,'Speed Stick','Aerosol de 200ml'),(2427,'Crema Blanqueadora Dental',NULL,8,90.00,50,NULL,NULL,'80048',NULL,'Crest','Tubo de 75ml'),(2428,'Mascarilla Facial Hidratante',NULL,8,100.00,40,NULL,NULL,'80049',NULL,'Glamglow','Tubo de 50g'),(2429,'Aceite para Masajes',NULL,8,65.00,55,NULL,NULL,'80050',NULL,'Vaseline','Botella de 200ml'),(2430,'Pizza de Pepperoni',NULL,9,70.00,40,NULL,NULL,'90001',NULL,'DiGiorno','Caja de 400g'),(2431,'Hamburguesas de Res',NULL,9,100.00,35,NULL,NULL,'90002',NULL,'BUBBA','Paquete de 10 unidades'),(2432,'Papas Fritas Congeladas',NULL,9,45.00,50,NULL,NULL,'90003',NULL,'McCain','Bolsa de 1kg'),(2433,'Filetes de Pescado',NULL,9,80.00,30,NULL,NULL,'90004',NULL,'Gorton\'s','Bolsa de 500g'),(2434,'Helado de Vainilla',NULL,9,60.00,25,NULL,NULL,'90005',NULL,'Ben & Jerry\'s','Envase de 500ml'),(2435,'Vegetales Mixtos',NULL,9,50.00,45,NULL,NULL,'90006',NULL,'Birds Eye','Bolsa de 800g'),(2436,'Nuggets de Pollo',NULL,9,70.00,40,NULL,NULL,'90007',NULL,'Tyson','Bolsa de 750g'),(2437,'Pan de Ajo Congelado',NULL,9,40.00,55,NULL,NULL,'90008',NULL,'New York Bakery Co.','Bolsa de 400g'),(2438,'Empanadas de Carne',NULL,9,90.00,60,NULL,NULL,'90009',NULL,'Tasty Bite','Paquete de 6 unidades'),(2439,'Helado de Chocolate',NULL,9,65.00,30,NULL,NULL,'90010',NULL,'Haagen-Dazs','Envase de 500ml'),(2440,'Tortellini de Queso',NULL,9,75.00,50,NULL,NULL,'90011',NULL,'Buitoni','Bolsa de 400g'),(2441,'Papas a la Francesa',NULL,9,55.00,40,NULL,NULL,'90012',NULL,'Ore-Ida','Bolsa de 900g'),(2442,'Pollo Empanizado',NULL,9,85.00,35,NULL,NULL,'90013',NULL,'Perdue','Bolsa de 800g'),(2443,'Pasta Alfredo con Pollo',NULL,9,60.00,40,NULL,NULL,'90014',NULL,'Lean Cuisine','Bolsa de 350g'),(2444,'Ensalada de Frutas Congeladas',NULL,9,50.00,45,NULL,NULL,'90015',NULL,'Dole','Bolsa de 600g'),(2445,'Hamburguesas Vegetarianas',NULL,9,80.00,30,NULL,NULL,'90016',NULL,'MorningStar Farms','Paquete de 8 unidades'),(2446,'Pizza de Queso',NULL,9,70.00,35,NULL,NULL,'90017',NULL,'Red Baron','Caja de 400g'),(2447,'Camarones Cocidos',NULL,9,90.00,25,NULL,NULL,'90018',NULL,'SeaPak','Bolsa de 500g'),(2448,'Helado de Fresa',NULL,9,55.00,30,NULL,NULL,'90019',NULL,'Breyers','Envase de 500ml'),(2449,'Espinacas Picadas',NULL,9,45.00,40,NULL,NULL,'90020',NULL,'Green Giant','Bolsa de 300g'),(2450,'Pechugas de Pollo',NULL,9,70.00,30,NULL,NULL,'90021',NULL,'Foster Farms','Bolsa de 900g'),(2451,'Palitos de Mozzarella',NULL,9,60.00,35,NULL,NULL,'90022',NULL,'Farm Rich','Bolsa de 400g'),(2452,'Ravioles de Carne',NULL,9,80.00,50,NULL,NULL,'90023',NULL,'Celentano','Bolsa de 400g'),(2453,'Papas a la Gajo',NULL,9,50.00,45,NULL,NULL,'90024',NULL,'Alexia','Bolsa de 800g'),(2454,'Pollo a la Parrilla',NULL,9,75.00,40,NULL,NULL,'90025',NULL,'Perdue','Bolsa de 900g'),(2455,'Lasagna de Carne',NULL,9,85.00,30,NULL,NULL,'90026',NULL,'Stouffer\'s','Bolsa de 400g'),(2456,'Helado de Cookies & Cream',NULL,9,60.00,25,NULL,NULL,'90027',NULL,'Blue Bell','Envase de 500ml'),(2457,'Brócoli Congelado',NULL,9,40.00,50,NULL,NULL,'90028',NULL,'Birds Eye','Bolsa de 500g'),(2458,'Empanadas de Pollo',NULL,9,80.00,55,NULL,NULL,'90029',NULL,'Tasty Bite','Paquete de 6 unidades'),(2459,'Tarta de Manzana',NULL,9,70.00,40,NULL,NULL,'90030',NULL,'Marie Callender\'s','Bolsa de 450g');
/*!40000 ALTER TABLE `productos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `registroactividades`
--

DROP TABLE IF EXISTS `registroactividades`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `registroactividades` (
  `RegistroID` int NOT NULL AUTO_INCREMENT,
  `UsuarioID` int DEFAULT NULL,
  `Acción` varchar(255) DEFAULT NULL,
  `FechaAcción` datetime DEFAULT NULL,
  `Descripción` text,
  PRIMARY KEY (`RegistroID`),
  KEY `UsuarioID` (`UsuarioID`),
  CONSTRAINT `registroactividades_ibfk_1` FOREIGN KEY (`UsuarioID`) REFERENCES `usuario` (`UsuarioID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `registroactividades`
--

LOCK TABLES `registroactividades` WRITE;
/*!40000 ALTER TABLE `registroactividades` DISABLE KEYS */;
/*!40000 ALTER TABLE `registroactividades` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `UsuarioID` int NOT NULL AUTO_INCREMENT,
  `NombreUsuario` varchar(255) DEFAULT NULL,
  `Contraseña` varchar(255) DEFAULT NULL,
  `Rol` varchar(50) DEFAULT NULL,
  `Email` varchar(255) DEFAULT NULL,
  `NombreCompleto` varchar(255) DEFAULT NULL,
  `UltimoLogin` datetime DEFAULT NULL,
  `PreguntaSeguridad` varchar(255) DEFAULT NULL,
  `RespuestaSeguridad` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UsuarioID`),
  UNIQUE KEY `NombreUsuario` (`NombreUsuario`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (13,'Administrador','','ADMINISTRADOR','brauliodamian80@gmail.com','Braulio Antonio Damian Gonzalez','2024-04-24 12:00:00','',''),(14,'Empleado1','a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3','EMPLEADO','accesoremoto377@gmail.com','Empleado Cualquiera',NULL,'¿Cuál es el nombre de tu primera mascota?|¿Cuál es tu película favorita?|¿En qué ciudad naciste?','a|a|a');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ventas`
--

DROP TABLE IF EXISTS `ventas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ventas` (
  `VentaID` int NOT NULL AUTO_INCREMENT,
  `UsuarioID` int DEFAULT NULL,
  `FechaVenta` datetime DEFAULT NULL,
  `PrecioTotal` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`VentaID`),
  KEY `UsuarioID` (`UsuarioID`),
  CONSTRAINT `ventas_ibfk_1` FOREIGN KEY (`UsuarioID`) REFERENCES `usuario` (`UsuarioID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ventas`
--

LOCK TABLES `ventas` WRITE;
/*!40000 ALTER TABLE `ventas` DISABLE KEYS */;
/*!40000 ALTER TABLE `ventas` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-06 11:18:54
