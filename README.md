# ads-stock

Stock centralizado de anuncios propios (house ads) para las apps de Lisandro.  
Sirve creatividades via GitHub Pages y expone el módulo Android via JitPack.

---

## Contenido del repo

```
ads-stock/
├── ads.json                  → catálogo de anuncios (leído por las apps)
├── banners/                  → imágenes PNG 320×50 px
├── interstitials/            → imágenes PNG 320×480 px
└── adsmanager/               → módulo Android (librería)
```

---

## Endpoint del catálogo

```
https://tuusuario.github.io/ads-stock/ads.json
```

Activa GitHub Pages en: **Settings → Pages → Source: main / root**

---

## Usar el módulo en tus apps (JitPack)

### settings.gradle
```groovy
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven { url 'https://jitpack.io' }
    }
}
```

### app/build.gradle
```groovy
dependencies {
    implementation 'com.github.tuusuario:ads-stock:1.0.0'
}
```

---

## Setup en cada app

### 1. AndroidManifest.xml
```xml
<uses-permission android:name="android.permission.INTERNET"/>
<application android:name=".MyApplication" ...>
```

### 2. MyApplication.kt
```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AdsManager.init(
            context = this,
            adsUrl  = "https://tuusuario.github.io/ads-stock/ads.json"
        )
    }
}
```

### 3. Layout XML
```xml
<com.lisandro.adsmanager.BannerAdView
    android:id="@+id/bannerAd"
    android:layout_width="match_parent"
    android:layout_height="50dp"
    app:layout_constraintBottom_toBottomOf="parent"/>
```

### 4. Activity
```kotlin
// Banner
bannerAd.load(lifecycleScope)

// Interstitial al salir
override fun onBackPressed() {
    if (AdFrequency.shouldShow()) {
        InterstitialAdHelper.show(this, lifecycleScope)
    }
    super.onBackPressed()
}
```

---

## Publicar nueva versión

```bash
git add .
git commit -m "feat: nueva creatividad"
git tag 1.1.0
git push origin main --tags
```

Luego en cada app: cambiar `1.0.0` → `1.1.0` en `build.gradle`.

---

## Campos de ads.json

| Campo | Tipo | Descripción |
|---|---|---|
| `id` | string | Identificador único |
| `app` | string | App que se promociona |
| `type` | string | `banner` o `interstitial` |
| `image_url` | string | URL de la imagen PNG |
| `action_url` | string | URL al hacer clic |
| `weight` | int | Peso de rotación (mayor = más impresiones) |
| `active` | bool | `false` para pausar sin borrar |
| `tags` | array | Categorías opcionales |
