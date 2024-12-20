app.controller('MuaNgayController', function ($scope, $routeParams, $http) {
    let id_HoaDonMuaNgay = localStorage.getItem('id_HoaDonMuaNgay');

    $http.get('http://localhost:8080/api/muaNgay/getHoaDon/' + id_HoaDonMuaNgay).then(function (response) {
        const hoaDon = response.data;
        $scope.tienTamTinh = hoaDon.tongTienHoaDon;
        $scope.tienShip = hoaDon.tienShip;
        $scope.tongTienHoaDon = hoaDon.tongTienDonHang;
        $scope.hoaDon = hoaDon;
    });

    $http.get('http://localhost:8080/api/muaNgay/getHoaDonChiTiet/' + id_HoaDonMuaNgay).then(function (response) {
        const hoaDonChiTiet = response.data;
        $scope.hoaDonChiTiet = hoaDonChiTiet;
    });



    function fomatTien(tien) {
        let chuoiDaLoaiBo = tien.replace(/\./g, '').replace(' ₫', '');
        return parseFloat(chuoiDaLoaiBo);
    }

    function api_datHang_MuaNgay() {
        let a = $('#total').text();
        let b = $('#shippingFee').text();
        let c = $('#subtotal').text();

        let tinhThanhPho = $('#province option:selected').text();
        let quanHuyen = $('#district option:selected').text();
        let phuongXa = $('#ward option:selected').text();
        let diaChiNhap = $scope.diaChi;
        let diaChi = diaChiNhap + ' - ' + phuongXa + ' - ' + quanHuyen + ' - ' + tinhThanhPho;

        let diaChi2;
        if (diaChi.includes('Chọn Tỉnh/Thành phố') || diaChi.includes('Chọn Quận/Huyện') || diaChi.includes('Chọn Phường/Xã')) {
            diaChi2 = '';
        } else {
            diaChi2 = diaChi;
        }

        const tongTienHoaDon = fomatTien(c);
        const tienShip = fomatTien(b);
        const tongTienDonHang = fomatTien(a);

        let data = {
            id: id_HoaDonMuaNgay,
            ghiChu: $scope.ghiChu,
            email: $scope.email,
            soDienThoai: $scope.soDienThoai,
            tienShip: tienShip,
            tongTienHoaDon: tongTienHoaDon,
            tongTienDonHang: tongTienDonHang,
            diaChi: diaChi2,
            nguoiTao: $scope.hoTen,
        };
        $http
            .post('http://localhost:8080/api/muaNgay/datHang', data)
            .then(function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Đặt hàng thành công',
                    showConfirmButton: false,
                    timer: 2000,
                }).then(() => {
                    window.location.href = 'http://127.0.0.1:5501/templates/customer/home/index.html#!/product-list';
                });
            })
            .catch(function (e) {
                const errorMessage = e.data[Object.keys(e.data)[0]];
                Swal.fire({
                    icon: 'error',
                    title: errorMessage,
                    showConfirmButton: false,
                    timer: 2000,
                });
            });
    }

    function api_thanhToan_MuaNgay() {
        let a = $('#total').text();
        let b = $('#shippingFee').text();
        let c = $('#subtotal').text();

        let tinhThanhPho = $('#province option:selected').text();
        let quanHuyen = $('#district option:selected').text();
        let phuongXa = $('#ward option:selected').text();
        let diaChiNhap = $scope.diaChi;
        let diaChi = diaChiNhap + ' - ' + phuongXa + ' - ' + quanHuyen + ' - ' + tinhThanhPho;

        let diaChi2;
        if (diaChi.includes('Chọn Tỉnh/Thành phố') || diaChi.includes('Chọn Quận/Huyện') || diaChi.includes('Chọn Phường/Xã')) {
            diaChi2 = '';
        } else {
            diaChi2 = diaChi;
        }

        const tongTienHoaDon = fomatTien(c);
        const tienShip = fomatTien(b);
        const tongTienDonHang = fomatTien(a);

        let data = {
            id: id_HoaDonMuaNgay,
            ghiChu: $scope.ghiChu,
            email: $scope.email,
            soDienThoai: $scope.soDienThoai,
            tienShip: tienShip,
            tongTienHoaDon: tongTienHoaDon,
            tongTienDonHang: tongTienDonHang,
            diaChi: diaChi2,
            nguoiTao: $scope.hoTen,
        };

        $http
            .post('http://localhost:8080/payment/MuaNgay/create', data)
            .then(function (response) {
                Swal.fire({
                    icon: 'success',
                    title: 'Đang chuyển hướng sang trang thanh toán',
                    showConfirmButton: false,
                    timer: 2000,
                }).then(() => {
                    window.location.href = response.data.createURL;
                });
            })
            .catch(function (e) {
                const errorMessage = e.data[Object.keys(e.data)[0]];
                Swal.fire({
                    icon: 'error',
                    title: errorMessage,
                    showConfirmButton: false,
                    timer: 2000,
                });
            });
    }

    $scope.datHang = function () {
        if (!$scope.email || !$scope.email.trim()) {
            Swal.fire({
                title: 'Cảnh báo',
                text: 'Nếu không điền email bạn sẽ không nhận được tình trạng đơn hàng, bạn có muốn tiếp tục không?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Đồng ý',
                cancelButtonText: 'Hủy',
            }).then((result) => {
                if (result.isConfirmed) {
                    api_datHang_MuaNgay();
                }
            });
        } else {
            Swal.fire({
                title: 'Xác nhận đặt hàng',
                text: 'Bạn có muốn đặt hàng không?',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Đồng ý',
                cancelButtonText: 'Hủy',
            }).then((result) => {
                if (result.isConfirmed) {
                    api_datHang_MuaNgay();
                }
            });
        }
    };

    $scope.thanhToan = function () {
        if (!$scope.email || !$scope.email.trim()) {
            Swal.fire({
                title: 'Cảnh báo',
                text: 'Nếu không điền email bạn sẽ không nhận được tình trạng đơn hàng, bạn có muốn tiếp tục không?',
                icon: 'warning',
                showCancelButton: true,
                confirmButtonText: 'Đồng ý',
                cancelButtonText: 'Hủy',
            }).then((result) => {
                if (result.isConfirmed) {
                    api_thanhToan_MuaNgay();
                }
            });
        } else {
            Swal.fire({
                title: 'Xác nhận đặt hàng',
                text: 'Bạn có muốn đặt hàng không?',
                icon: 'success',
                showCancelButton: true,
                confirmButtonText: 'Đồng ý',
                cancelButtonText: 'Hủy',
            }).then((result) => {
                if (result.isConfirmed) {
                    api_thanhToan_MuaNgay();
                }
            });
        }
    };

    $scope.quayLai = function () {
        window.location.href = 'http://127.0.0.1:5501/templates/customer/home/index.html#!/product-list';
    };
});
