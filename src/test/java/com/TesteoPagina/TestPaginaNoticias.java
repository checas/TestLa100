package com.TesteoPagina;

import org.testng.annotations.Test;

import org.testng.annotations.BeforeClass;

import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterClass;

public class TestPaginaNoticias {
	private String filepath = "C:\\Users\\yo_ch\\Desktop\\workcloud\\testing\\Test_2.xlsx";
	private int filaActual = 6;
	private WebDriver driver;
	private ReadExcel readFile;
	private WriteExcel writeFile;
	private By titulo = By.xpath("//div[@class=\"articleContainer\"]//h1");
	private By subTitulo = By.xpath("//div[@class=\"articleContainer\"]//h2");
	private By fecha = By
			.xpath("//div[@class=\"articleContainer\"]//span[@class=\"articleContainer__header__top__date\"]");
	private By aLinks = By.xpath("//div[@class=\"articleContainer\"]//a");
	private By videos = By.xpath(
			"//div[@class=\"articleContainer\"]//div[@class=\"video-inner-container\"]//video[not(@title=\"Advertisement\")]");
	private By imagenes = By.xpath("//div[@class=\"articleContainer\"]//div[@class=\"articleImgContent\"]//img");
	private By miraTmb = By.xpath("//div[@class=\"articleContainer\"]//div[@class=\"articleReferenceContainer\"]/a");
	private By texto = By.xpath("//div[@class=\"articleContainer\"]//section[@class=\"paragrath\"]/div");
	private By divImg = By.cssSelector(".articleContainer .articleImgContent");
	private boolean todoOK = true;
	private boolean tituloOk = false;
	private boolean subTituloOk = false;
	private boolean imagenesOk = false;
	private boolean videosOk = false;
	private boolean miraTmbOk = false;
	private boolean linksOk = false;
	private boolean textoOk = false;

	@BeforeClass
	public void beforeClass() throws IOException {
		System.setProperty("webdriver.chrome.driver", "./src/test/resources/chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().window().maximize();
		readFile = new ReadExcel();
		writeFile = new WriteExcel();
		String searchText = readFile.getCellValue(filepath, "Hoja1", filaActual, 2);
		System.out.println("Lectura excel: " + searchText);
		driver.get(searchText);
		// driver.get("https://la100.cienradios.com/espectaculos/a-puro-escote-sinuoso-y-peligroso-noelia-marzol-encandilo-la-noche/");
		// driver.get("https://la100.cienradios.com/le-dijeron-que-tenia-cancer-le-extirparon-los-pechos-e-hizo-quimioterapia-todo-fue-un-mal-diagnostico/");
		// driver.get("https://la100.cienradios.com/le-dijeron-que-tenia-cancer-le-extirparon-los-pechos-e-hizo-quimioterapia-todo-fue-un-mal-diagnostico/");
	}

	@Test
	public void paginaNoticias() throws IOException, InterruptedException {
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		// Get Fecha
		System.out.println(driver.findElement(fecha).getText());

		// Variables
		String tagTitulo = driver.findElement(titulo).getTagName();

		// Prueba de titulo
		System.out.println("=========================================================");
		System.out.println("Titulo de página: " + driver.findElement(titulo).getText());
		System.out.println("Fecha: " + driver.findElement(fecha).getText());
		System.out.println("=========================================================");
		System.out.println("Tag titulo: " + tagTitulo);
		System.out.println("---------------------------------------------------------");

		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 3, driver.findElement(fecha).getText());
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 4, tagTitulo);

		tituloOk = tagTitulo.equals("h1");
		if (tituloOk) {
			System.out.println("Prueba titulo aprovada");
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 5, "SI");
		} else {
			System.out.println("ERROR --- Prueba titulo no aprobada");
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 5, "NO");
			todoOK = false;
		}
		System.out.println("---------------------------------------------------------");

		// Prueba de subtitulo
		String tagSubtitulo = "";
		try {
			tagSubtitulo = driver.findElement(subTitulo).getTagName();
		} catch (Exception e) {
			tagSubtitulo = "";
		}

		if (tagSubtitulo.equals("")) {
			System.out.println("Subtitulo no encontrado con etiqueta h2");
			subTituloOk = false;
			todoOK = false;

		} else {
			System.out.println("Tag subtitulo: " + tagSubtitulo);
			subTituloOk = tagSubtitulo.equals("h2");
		}
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 6, tagSubtitulo);
		if (subTituloOk) {
			System.out.println("Prueba subtitulo aprovada");
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 7, "SI");
		} else {
			System.out.println("ERROR --- Prueba subtitulo no aprobada");
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 7, "NO");
			todoOK = false;
		}
		System.out.println("---------------------------------------------------------");

		// Prueba imagenes
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Boolean pasaImagen = true;
		List<WebElement> ListaImagenes = driver.findElements(imagenes);
		Iterator<WebElement> itImg = ListaImagenes.iterator();
		int iAux = 0;
		System.out.println("Cantidad de imgenes " + ListaImagenes.size());
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 8, String.valueOf(ListaImagenes.size()));
		while (itImg.hasNext()) {
			iAux++;
			// pasaImagen = false;
			WebElement imgAux = itImg.next();
			String alto = imgAux.getCssValue("height");
			String ancho = imgAux.getCssValue("width");
			System.out.println("Imagen " + iAux + "/" + ListaImagenes.size());
			System.out.println("Alto: " + alto);
			System.out.println("Ancho: " + ancho);
			double dAlto;
			double dAncho;
			if (!(alto).equals("auto")) {
				dAlto = Double.parseDouble(alto.substring(0, alto.indexOf("px")));
				if (dAlto >= 1000)
					pasaImagen = false;
				System.out.println(dAlto);
			}
			if (!(ancho).equals("auto")) {
				dAncho = Double.parseDouble(ancho.substring(0, ancho.indexOf("px")));
				if (dAncho >= 1000)
					pasaImagen = false;
				System.out.println(dAncho);
			}
			if (pasaImagen) {
				System.out.println("Prueba de imagen aprobada");
				writeFile.writeCellValue(filepath, "Hoja1", filaActual, 9, "SI");
				writeFile.writeCellValue(filepath, "Hoja1", filaActual, 10, "SI");
			} else {
				System.out.println("Prueba de imagen fallada");
				writeFile.writeCellValue(filepath, "Hoja1", filaActual, 9, "NO");
				writeFile.writeCellValue(filepath, "Hoja1", filaActual, 10, "NO");
				todoOK = false;
			}
		}
		System.out.println("---------------------------------------------------------");

		// Prueba videos
		List<WebElement> ListaVideos = driver.findElements(videos);
		Iterator<WebElement> itVideo = ListaVideos.iterator();
		iAux = 0;
		System.out.println("Cantidad de videos " + ListaVideos.size());
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 11, String.valueOf(ListaVideos.size()));
		while (itVideo.hasNext()) {
			iAux++;
			WebElement videoAux = itVideo.next();
			System.out.println("Video " + iAux + "/" + ListaVideos.size());
			System.out.println("Src: " + videoAux.getAttribute("src"));
			System.out.println("Title: " + videoAux.getAttribute("title"));
			// String videoTitle = videoAux.getAttribute("title");
		}
		System.out.println("---------------------------------------------------------");

		// Prueba mira tmb
		List<WebElement> ListMiraTbm = driver.findElements(miraTmb);
		Iterator<WebElement> itMiraTmb = ListMiraTbm.iterator();
		boolean pasaMiraTmb = true;
		iAux = 0;
		int miraRoto = 0;
		System.out.println("Cantidad de mira tmb: " + ListMiraTbm.size());
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 14, String.valueOf(ListMiraTbm.size()));
		while (itMiraTmb.hasNext()) {
			iAux++;
			WebElement miraAux = itMiraTmb.next();
			System.out.println("Mira tmb " + iAux + "/" + ListMiraTbm.size());
			System.out.println("Src: " + miraAux.getAttribute("href"));
			if (!revisarLinks(miraAux.getAttribute("href"))) {
				pasaMiraTmb = false;
				miraRoto++;
			}
		}
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 15, String.valueOf(miraRoto));
		if (pasaMiraTmb) {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 16, "SI");
		} else {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 16, "NO");
			todoOK = false;
		}
		System.out.println("---------------------------------------------------------");

		// Pruba de links
		List<WebElement> ListaLinks = driver.findElements(aLinks);
		Iterator<WebElement> itLink = ListaLinks.iterator();
		boolean pasaLink = true;
		int linkRoto = 0;
		iAux = 0;
		System.out.println("Cantidad de links: " + ListaLinks.size());
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 17, String.valueOf(ListaLinks.size()));
		while (itLink.hasNext()) {
			iAux++;
			WebElement linkAux = itLink.next();
			System.out.println("Mira tmb " + iAux + "/" + ListaLinks.size());
			System.out.println("Src: " + linkAux.getAttribute("href"));
			if (!revisarLinks(linkAux.getAttribute("href"))) {
				pasaLink = false;
				linkRoto++;
			}
		}
		writeFile.writeCellValue(filepath, "Hoja1", filaActual, 18, String.valueOf(miraRoto));
		if (pasaLink) {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 19, "SI");
		} else {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 19, "NO");
			todoOK = false;
		}
		System.out.println("---------------------------------------------------------");

		// Prueba parrafos
		List<WebElement> ListaParrafos = driver.findElements(texto);
		Iterator<WebElement> itParrafo = ListaParrafos.iterator();
		iAux = 0;
		boolean sinPlano = true;
		boolean pasaTexto = true;
		System.out.println("Cantidad de parrafos: " + ListaParrafos.size());
		while (itParrafo.hasNext()) {
			iAux++;
			pasaTexto = true;
			WebElement parrafoAux = itParrafo.next();
			System.out.println("Mira tmb " + iAux + "/" + ListaParrafos.size());
			System.out.println("Texto: " + parrafoAux.getText());

			String textoARevisar = parrafoAux.getText();
			int linkPlano = textoARevisar.indexOf("http");
			System.out.println("indice http: " + linkPlano);
			if ((linkPlano == -1) || (linkPlano != 0)) {
				pasaTexto = true;
				System.out.println("Texto OK!");
			} else {
				pasaTexto = false;
				System.out.println("Error Texto");
				sinPlano = false;
			}
		}
		if (sinPlano) {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 20, "NO");
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 21, "SI");
		} else {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 20, "SI");
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 21, "NO");
			todoOK = false;
		}

		System.out.println("---------------------------------------------------------");

		if (todoOK) {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 22, "SI");
		} else {
			writeFile.writeCellValue(filepath, "Hoja1", filaActual, 22, "NO");
		}
		/*
		 * tituloOk = tagTitulo.equals("h1"); subTituloOk = tagSubtitulo.equals("h2");
		 */
		// assertTrue(driver.findElement(titulo).getTagName().equals("h1"), "Fallo el
		// tag del titulo o del sub titulo");
		assertTrue(todoOK, "Fallo en alguna prueba");
	}

	@AfterClass
	public void afterClass() {
		driver.quit();
	}

	public boolean revisarLinks(String url) {
		boolean estado = false;
		if (url != null) {
			HttpURLConnection httpConection = null;
			int responseCode = 200;
			try {
				httpConection = (HttpURLConnection) (new URL(url).openConnection());
				httpConection.setRequestMethod("HEAD");
				httpConection.connect();
				responseCode = httpConection.getResponseCode();
				if (responseCode > 400) {
					if (responseCode != 405) {
						System.out.println("ERROR BROKEN LINK: " + responseCode + "-- " + url);
						estado = false;
					}
				} else {
					System.out.println("VALID LINK: -- " + url);
					estado = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Link vacio");
			estado = false;
		}
		return estado;
	}
}
