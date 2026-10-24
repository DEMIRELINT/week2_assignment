# week2_assignment — DevCluster

Kotlin ve Jetpack Compose ile geliştirilen donanım ve sunucu kaynak yönetim simülatörü.

## Uygulama

- Ödevde verilen sekiz donanımı isim, kategori, fiyat ve stok bilgileriyle listeler.
- Donanım adına veya kategorisine göre arama ve kategori filtreleme yapar.
- Bir karta dokunulduğunda bir adet donanım tahsis eder; stok azalır ve harcanan ödenek güncellenir.
- Stok tükenen donanımlarda tahsis işlemini engeller.
- Ekran döndürüldüğünde arama metnini, seçili kategoriyi, stokları ve harcama bilgisini korur.

## Teknik yapı

- `HardwareItem`, `@Immutable` ile işaretlenmiştir ve tüm alanları `val` olarak tanımlıdır.
- Durumlar merkezi `DevClusterScreen` bileşeninde yönetilir; harcama `remember`, arama `rememberSaveable` kullanır.
- Arama ve kategori filtreleme sonuçları `remember` ile önbelleğe alınır.
- Donanımlar `LazyColumn` içinde `key = { it.id }` ile gösterilir.
- `HardwareCard` ve `HardwareSearchBar` stateless bileşenlerdir; durumları parametreyle alır, olayları callback ile iletir.
- Kartlarda `clickable` ardından `padding` uygulanır.

Ana dosya: `app/src/main/java/com/example/week2/MainActivity.kt`

Projeyi Android Studio'da açıp Gradle eşitlemesinden sonra `app` modülünü çalıştırın.
