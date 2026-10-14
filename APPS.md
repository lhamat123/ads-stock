# Registro de Apps y Recursos

Cada app que quieras promocionar necesita una entrada aquí y en `ads.json`.  
Las imágenes van en `banners/` o `interstitials/` con el nombre exacto que se referencia en el JSON.

---

## Apps activas

### MeshAlarm
| Campo | Valor |
|---|---|
| ID | `meshalarm` |
| Descarga | https://tuapk.com/meshalarm |
| Sitio web | — |
| Banner | `banners/meshalarm_banner.png` |
| Interstitial | `interstitials/meshalarm_full.png` |
| Estado | ✅ Activo |

---

### Cotizaló
| Campo | Valor |
|---|---|
| ID | `cotizalo` |
| Descarga | — |
| Sitio web | https://cotizalobien.com |
| Banner | `banners/cotizalo_banner.png` |
| Interstitial | — (pendiente) |
| Estado | ✅ Activo |

---

### Mi Horario
| Campo | Valor |
|---|---|
| ID | `mihorario` |
| Descarga | https://tuapk.com/mihorario |
| Sitio web | — |
| Banner | `banners/mihorario_banner.png` |
| Interstitial | — (pendiente) |
| Estado | ✅ Activo |

---

## Plantilla para nueva app

Copia esto cuando agregues una app nueva:

### NombreApp
| Campo | Valor |
|---|---|
| ID | `nombre_id` |
| Descarga | https:// |
| Sitio web | https:// |
| Banner | `banners/nombre_banner.png` |
| Interstitial | `interstitials/nombre_full.png` |
| Estado | 🔲 Pendiente / ✅ Activo / ⏸ Pausado |

Y en `ads.json` añade:

```json
{
  "id": "nombre_id",
  "name": "NombreApp",
  "description": "Descripción corta",
  "download_url": "https://",
  "website_url": "",
  "active": true,
  "ads": [
    {
      "id": "nombre_banner_01",
      "type": "banner",
      "image": "banners/nombre_banner.png",
      "weight": 1,
      "active": true
    }
  ]
}
```

---

## URLs base

| Recurso | URL |
|---|---|
| Catálogo JSON | https://tuusuario.github.io/ads-stock/ads.json |
| Banners | https://tuusuario.github.io/ads-stock/banners/ |
| Interstitials | https://tuusuario.github.io/ads-stock/interstitials/ |

> Reemplaza `tuusuario` con tu usuario real de GitHub.
