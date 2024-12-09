const urlAlumnos = "/v1/alumnos"; 
const urlCarreras = "/v1/carreras"; 

let carrerasMap = {};

function ajaxRequest(type, endpoint, data = null) {
    return $.ajax({
        type,
        url: endpoint,
        data: data ? JSON.stringify(data) : null,
        dataType: "json",
        contentType: data ? "application/json" : undefined,
        cache: false,
        timeout: 600000,
    });
}

function saveAlumno(bandera) {
    const id = $("#guardar").data("id");
    const alumno = {
        id,
        nombre: $("#nombre").val(),
        apellido: $("#apellido").val(),
        dni: $("#dni").val(),
        email: $("#email").val(),
        telefono: $("#telefono").val(),
        carreraId: $("#carrera").val() 
    };

    const type = bandera === 1 ? "POST" : "PUT";
    const endpoint = bandera === 1 ? urlAlumnos : `${urlAlumnos}/${id}`;

    ajaxRequest(type, endpoint, alumno)
        .done((data) => {
            if (data.ok) {
                $("#modal-update").modal("hide");
                getTablaAlumnos();
                $("#error-message").addClass("d-none");
                Swal.fire({
                    icon: 'success',
                    title: `Se ha ${bandera === 1 ? 'guardado' : 'actualizado'} el alumno`,
                    showConfirmButton: false,
                    timer: 1500
                });
                clearAlumno();
            } else {
                showError(data.message);
            }
        })
        .fail(function (jqXHR) {
            const errorMessage = jqXHR.responseJSON?.message || "Error inesperado. Código: " + jqXHR.status;
            showError(errorMessage);
        });
}

function showError(message) {
    $("#error-message").text(message).removeClass("d-none");
}

function deleteAlumno(id) {
    ajaxRequest("DELETE", `${urlAlumnos}/${id}`)
        .done((data) => {
            if (data.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Se ha eliminado el alumno',
                    showConfirmButton: false,
                    timer: 1500
                });
                getTablaAlumnos();
            } else {
                Swal.fire({
                    icon: 'error',
                    title: 'Error',
                    text: data.message,
                    confirmButtonText: 'Aceptar'
                });
            }
        })
        .fail(handleError);
}

function getTablaAlumnos() {
    ajaxRequest("GET", urlAlumnos)
        .done((data) => {
            const t = $("#tablaRegistros").DataTable();
            t.clear();

            if (data.ok) {
                $.each(data.body, (index, alumno) => {
                    const botonera = `
                        <button type="button" class="btn btn-warning btn-xs editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs eliminar">
                            <i class="fas fa-trash"></i>
                        </button>`;

                    
                    const carreraNombre = carrerasMap[alumno.carreraId] || 'Sin carrera'; 

                    t.row.add([botonera, alumno.id, alumno.nombre, alumno.apellido, alumno.dni, alumno.email, alumno.telefono, carreraNombre]);
                });
                t.draw();
            } else {
                console.error("Error en la respuesta: ", data.message);
            }
        })
        .fail(handleError);
}

function getAlumnoFila(id) {
    ajaxRequest("GET", `${urlAlumnos}/${id}`)
        .done((data) => {
            if (data.ok) {
                $("#modal-title").text("Editar alumno");
                $("#nombre").val(data.body.nombre);
                $("#apellido").val(data.body.apellido);
                $("#dni").val(data.body.dni);
                $("#email").val(data.body.email);
                $("#telefono").val(data.body.telefono);
                $("#carrera").val(data.body.carreraId);
                $("#guardar").data("id", data.body.id).data("bandera", 0);
                $("#modal-update").modal("show");
            } else {
                showError(data.message);
            }
        })
        .fail(handleError);
}

function clearAlumno() {
    $("#modal-title").text("Nuevo alumno");
    $("#nombre").val("");
    $("#apellido").val("");
    $("#dni").val("");
    $("#email").val("");
    $("#telefono").val("");
    $("#carrera").val(""); 
    $("#guardar").data("id", 0).data("bandera", 1);
}

function handleError(jqXHR) {
    const errorMessage = jqXHR.responseJSON?.message || `Error inesperado. Código: ${jqXHR.status}`;
    Swal.fire({
        icon: 'error',
        title: 'Error',
        text: errorMessage,
        confirmButtonText: 'Aceptar'
    });
}

$(document).ready(function () {
    const t = $("#tablaRegistros").DataTable({
        language: {
            lengthMenu: "Mostrar _MENU_ alumnos",
            zeroRecords: "No se encontraron coincidencias",
            info: "Mostrando del _START_ al _END_ de _TOTAL_ alumnos",
            infoEmpty: "Sin resultados",
            search: "Buscar: ",
            paginate: {
                first: "Primero",
                last: "Último",
                next: "Siguiente",
                previous: "Anterior",
            },
        },
        columnDefs: [
            { targets: 0, orderable: false }
        ],
    });

    clearAlumno();

    $("#nuevo").click(clearAlumno);

    $("#guardar").click(() => saveAlumno($("#guardar").data("bandera")));

    $(document).on('click', '.eliminar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        Swal.fire({
            title: 'Eliminar alumno',
            text: "¿Está seguro de querer eliminar este alumno?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteAlumno(id);
            }
        });
    });

    $(document).on('click', '.editar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        getAlumnoFila(id);
    });

    $.ajax({
        url: urlCarreras,
        method: 'GET',
        success: function(carreras) {
            var carreraSelect = $('#carrera');
            carreraSelect.empty();
            if (carreras.ok) {
                $.each(carreras.body, function(index, carrera) {
                    carreraSelect.append($('<option>', {
                        value: carrera.id,
                        text: carrera.nombre 
                    }));
                    carrerasMap[carrera.id] = carrera.nombre; 
                });
            } else {
                console.error('Error al cargar carreras:', carreras.message);
            }
        },
        error: function(err) {
            console.error('Error al cargar carreras:', err);
        }
    });

    getTablaAlumnos(); 
});
