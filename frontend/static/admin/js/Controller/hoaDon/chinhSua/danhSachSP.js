app.controller('danhSachSanPhamHoaDonController', function ($scope, $http) {
    let token = localStorage.getItem('token');
    let id_hoa_don_chinh_sua = localStorage.getItem('id_hoa_don_chinh_sua');
    let headers = {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token,
    };

    function parseJwt(token) {
        let base64Url = token.split('.')[1];
        let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        let jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map(function (c) {
                    return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                })
                .join(''),
        );

        let payload = JSON.parse(jsonPayload);
        return payload;
    }

    let decodedToken = parseJwt(token);

    $http.get('http://localhost:8080/customer/sanPham/danhSach', { headers }).then(function (response) {
        const sanPham = response.data;
        $scope.sanPham = sanPham;
    });

    $scope.pager = {
        page: 1,
        size: 12,
        get sanPham() {
            if ($scope.sanPham && $scope.sanPham.length > 0) {
                let start = (this.page - 1) * this.size;
                return $scope.sanPham.slice(start, start + this.size);
            } else {
                return [];
            }
        },
        get count() {
            if ($scope.sanPham && $scope.sanPham.length > 0) {
                let start = (this.page - 1) * this.size;
                return Math.ceil((1.0 * $scope.sanPham.length) / this.size);
            } else {
                // Trả về 0
                return 0;
            }
        },
        get pageNumbers() {
            const pageCount = this.count;
            const pageNumbers = [];
            for (let i = 1; i <= pageCount; i++) {
                pageNumbers.push(i);
            }
            return pageNumbers;
        },
    };

    $scope.getSanPhamChiTiet = function (sanPham) {
        let id_sanPham = sanPham.id;
        window.location.href = '#!/chiTietSP_hoaDon?id=' + id_sanPham;
    };


    $http.get('http://localhost:8080/loaiSanPham/danhSach', { headers }).then(function (response) {
        const loaiSanPham = response.data;
        $scope.loaiSanPham = loaiSanPham;
    });

    $scope.filterByLoaiSp = function () {
        $http
            .get('http://localhost:8080/customer/sanPham/loc/loai_san_pham', {
                params: { idloaiSanPham: $scope.idloaiSanPham },
                headers: headers,
            })
            .then(function (response) {
                const sanPham = response.data;
                $scope.sanPham = sanPham;
            });
    };


    $scope.filterByGia = function () {
        $http
            .get('http://localhost:8080/customer/sanPham/loc/gia', {
                params: { gia1: $scope.gia1, gia2: $scope.gia2 },
                headers: headers,
            })
            .then(function (response) {
                $scope.sanPham = response.data;
            });
    };

    $scope.$watch('searchTerm', function (newVal) {
        if (newVal) {
            $http.get('http://localhost:8080/customer/sanPham/timKiemTheoTen/' + newVal, { headers }).then(function (response) {
                $scope.sanPham = response.data;
            });
        } else {
            $http.get('http://localhost:8080/customer/sanPham/danhSach', { headers }).then(function (response) {
                $scope.sanPham = response.data;
            });
        }
    });

    $scope.searchAll = function (searchTerm) {
        $http.get('http://localhost:8080/customer/sanPham/timKiemTheoTen/' + searchTerm, { headers }).then(function (response) {
            $scope.sanPham = response.data;
        });
    };

    $scope.reLoad = function () {
        $http.get('http://localhost:8080/customer/sanPham/danhSach', { headers }).then(function (response) {
            const sanPham = response.data;
            $scope.$evalAsync(function () {
                $scope.sanPham = sanPham;
            });
        });
    };

    $scope.quayVe = function () {
        window.location.href = '#!/CTChoXacNhan?id=' + id_hoa_don_chinh_sua;
    };
});

app.controller('ChiTietSanPhamHoaDonController', function ($scope, $routeParams, $http, $window) {
    const id_sanPham = $routeParams.id;
    const id_hoa_don_chinh_sua = localStorage.getItem('id_hoa_don_chinh_sua');
    let token = localStorage.getItem('token');
    let headers = {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token,
    };

    let decodedToken;
    if (token) {
        function parseJwt(token) {
            let base64Url = token.split('.')[1];
            let base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
            let jsonPayload = decodeURIComponent(
                atob(base64)
                    .split('')
                    .map(function (c) {
                        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
                    })
                    .join(''),
            );

            let payload = JSON.parse(jsonPayload);
            return payload;
        }

        decodedToken = parseJwt(token);
    }

    $http.get('http://localhost:8080/customer/sanPham/getSanPham/id=' + id_sanPham, { headers }).then(function (response) {
        const sanPham = response.data;
        $scope.sanPham = sanPham;
    });

    // $http
    //     .get('http://localhost:8080/customer/sanPham/api/getSize/' + id_sanPham, {
    //         headers,
    //     })
    //     .then(function (response) {
    //         const kichCo = response.data;
    //         $scope.kichCo = kichCo;
    //     });

    // $http.get('http://localhost:8080/customer/sanPham/api/getColor/' + id_sanPham, { headers }).then(function (response) {
    //     const mauSac = response.data;
    //     $scope.mauSac = mauSac;
    // });

    $http.get('http://localhost:8080/customer/sanPham/getAnhSanPham/' + id_sanPham).then(function (response) {
        const hinhAnh_list = response.data;
        $scope.hinhAnhs = hinhAnh_list;
    });

    $http.get('http://localhost:8080/customer/sanPham/getAnhMacDinhSanPham/' + id_sanPham).then(function (response) {
        const hinhAnh = response.data.anhMacDinh;
        $scope.hinhAnh = hinhAnh;
    });

    // $scope.selectKichCo = function (kichCo) {
    //     $scope.selectedKichCo = kichCo;
    //     return $scope.selectedKichCo;
    // };

    // $scope.selectMauSac = function (mauSac) {
    //     $scope.selectedMauSac = mauSac;
    // };

    let soLuongGet;
    let kichCo;
    let maMauSac;

    let soLuongHienCo;

    $scope.$watchGroup(['selectedKichCo', 'selectedMauSac'], function (newValues, oldValues) {
        if (newValues[0] !== undefined && newValues[1] !== undefined) {
            let data = {
                // kichCo: newValues[0],
                // maMauSac: newValues[1],
                sanPhamId: $scope.sanPham.id,
            };
            kichCo = newValues[0];
            maMauSac = newValues[1];
            $http.post('http://localhost:8080/customer/sanPham/api/getSoLuong', data, { headers }).then(function (response) {
                soLuongGet = document.getElementById('customer-sanPham-soLuongHienCo');
                if (soLuongGet) {
                    soLuongGet.innerText = response.data;
                    soLuongHienCo = response.data;
                }
            });
        }
    });

    // $scope.maMauSac = '';
    // $scope.$watch('selectedMauSac', function (newVal, oldVal) {
    //     if (newVal !== undefined) {
    //         $scope.maMauSac = newVal;
    //         let data = {
    //             id_SP: id_sanPham,
    //             maMauSac: $scope.maMauSac,
    //         };
    //         $http.post('http://localhost:8080/customer/sanPham/getAnhByMauSac', data).then(function (response) {
    //             const hinhAnh = response.data.anh;
    //             $scope.hinhAnh = hinhAnh;
    //         });
    //     }
    // });

    $scope.quayLai = function () {
        $window.history.back();
    };

    $scope.themSanPham = function (sanPham) {
        if ($scope.chonSoLuong > soLuongHienCo) {
            Swal.fire({
                icon: 'error',
                title: 'Số lượng thêm không được lớn hơn số lượng hiện có',
                showConfirmButton: false,
                timer: 2000,
            });
            $scope.chonSoLuong = soLuongHienCo;
            return;
        } else if ($scope.chonSoLuong < 0) {
            Swal.fire({
                icon: 'error',
                title: 'Số lượng thêm không được nhỏ hơn 0',
                showConfirmButton: false,
                timer: 2000,
            });
            $scope.chonSoLuong = 1;
            return;
        } else {
            let data = {
                id_hoaDon: id_hoa_don_chinh_sua,
                // kichCoDaChon: kichCo,
                // maMauSac: maMauSac,
                san_pham_id: sanPham.id,
                soLuong: $scope.chonSoLuong,
                donGia: sanPham.gia,
                email_user: decodedToken.email,
            };

            $http
                .post('http://localhost:8080/hoaDon/ChinhSua/themSanPham', data, {
                    headers,
                })
                .then(function (response) {
                    Swal.fire({
                        icon: 'success',
                        title: 'Thêm sản phẩm thành công',
                        showConfirmButton: false,
                        timer: 2000,
                    }).then(() => {
                        window.location.href = '#!/CTChoXacNhan?id=' + id_hoa_don_chinh_sua;
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
    };
});
