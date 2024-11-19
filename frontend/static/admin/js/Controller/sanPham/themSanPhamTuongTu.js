app.controller('themSanPhamTuongTuController', function ($scope, $routeParams, $http, $location) {
    let id_sanPham = $routeParams.id;
    let token = localStorage.getItem('token');
    let headers = {
        'Content-Type': 'application/json',
        Authorization: 'Bearer ' + token,
    };

    $http.get('http://localhost:8080/sanPhamChiTiet/chinhSua/' + id_sanPham, { headers }).then(function (response) {
        const editproduct = response.data;
        $scope.editproduct = editproduct;
    });


    $scope.LuuThemMoiSanPhamTuongTu = function () {
        let data = {
            id: id_sanPham,
            mauSac_id: $scope.mauSacDaChon,
            // kichCo_id: $scope.kichCocDaChon,
            soLuong: $scope.editproduct.soLuong,
        };
        $http
            .post('http://localhost:8080/sanPham/themSanPhamTuongTu', data, { headers })
            .then(function (response) {
                Swal.fire({
                    icon: 'success',
                    title: response.data.success,
                    showConfirmButton: false,
                    timer: 2000,
                }).then(function () {
                    sessionStorage.setItem('isConfirmed', true);
                    let idProduct = $routeParams.id;
                    window.location.href = '#!/list-CTSP?id=' + idProduct;
                });
            })
            .catch(function (errorResponse) {
                console.error('Error Response:', errorResponse);

                if (errorResponse.status === 400) {
                    const errorMessage = errorResponse.data.err;
                    showError(errorMessage);
                } else {
                    showError('Vui lòng nhập thông tin sản phẩm muốn thêm');
                }
            });

        function showError(errorMessage) {
            Swal.fire({
                icon: 'error',
                title: errorMessage,
                showConfirmButton: false,
                timer: 2000,
            });
        }
    };

    $scope.returnEdit = function () {
        let idProduct = $routeParams.id;
        $location.path('/list-CTSP').search({ id: idProduct });
    };
});
