const url = "/v1/docentes"; 
const urlCursos = "/v1/cursos";

let cursosMap = {};

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

function save(bandera) {
    const id = $("#guardar").data("id");
    const docente = {
        id,
        nombre: $("#nombre").val(),
        apellido: $("#apellido").val(),
        telefono: $("#telefono").val(),
        email: $("#email").val(),
        cursoId: $("#curso").val()
    };

    const type = bandera === 1 ? "POST" : "PUT";
    const endpoint = bandera === 1 ? url : `${url}/${id}`;

    ajaxRequest(type, endpoint, docente)
        .done((data) => {
            if (data.ok) {
                $("#modal-update").modal("hide");
                getTabla();
                $("#error-message").addClass("d-none");
                Swal.fire({
                    icon: 'success',
                    title: `Se ha ${bandera === 1 ? 'guardado' : 'actualizado'} el docente`,
                    showConfirmButton: false,
                    timer: 1500
                });
                clear();
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

function deleteFila(id) {
    ajaxRequest("DELETE", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                Swal.fire({
                    icon: 'success',
                    title: 'Se ha eliminado el docente',
                    showConfirmButton: false,
                    timer: 1500
                });
                getTabla();
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

function getTabla() {
    ajaxRequest("GET", url)
        .done((data) => {
            const t = $("#tablaRegistros").DataTable();
            t.clear();

            if (data.ok) {
                $.each(data.body, (index, docente) => {
                    const botonera = `
                        <button type="button" class="btn btn-warning btn-xs editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs eliminar">
                            <i class="fas fa-trash"></i>
                        </button>`;
                    
                    const cursoNombre = cursosMap[docente.cursoId] || 'Sin curso';
                    
                    t.row.add([botonera, docente.id, docente.nombre, docente.apellido, docente.telefono, docente.email, cursoNombre]);
                });
                t.draw(); 
            } else {
                console.error("Error en la respuesta: ", data.message);
            }
        })
        .fail(handleError);
}

function getFila(id) {
    ajaxRequest("GET", `${url}/${id}`)
        .done((data) => {
            if (data.ok) {
                $("#modal-title").text("Editar docente");
                $("#nombre").val(data.body.nombre);
                $("#apellido").val(data.body.apellido);
                $("#telefono").val(data.body.telefono);
                $("#email").val(data.body.email);
                $("#curso").val(data.body.cursoId);
                $("#guardar").data("id", data.body.id).data("bandera", 0);
                $("#modal-update").modal("show");
            } else {
                showError(data.message);
            }
        })
        .fail(handleError);
}

function clear() {
    $("#modal-title").text("Nuevo docente");
    $("#nombre").val("");
    $("#apellido").val("");
    $("#telefono").val("");
    $("#email").val("");
    $("#curso").val("");
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
            lengthMenu: "Mostrar _MENU_ docentes",
            zeroRecords: "No se encontraron coincidencias",
            info: "Mostrando del _START_ al _END_ de _TOTAL_ docentes",
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

    clear();

    $("#nuevo").click(clear);

    $("#guardar").click(() => save($("#guardar").data("bandera")));

    $(document).on('click', '.eliminar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        Swal.fire({
            title: 'Eliminar docente',
            text: "¿Está seguro de querer eliminar este docente?",
            icon: 'warning',
            showCancelButton: true,
            confirmButtonColor: '#3085d6',
            cancelButtonColor: '#d33',
            confirmButtonText: 'Si'
        }).then((result) => {
            if (result.isConfirmed) {
                deleteFila(id);
            }
        });
    });

    $(document).on('click', '.editar', function () {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        getFila(id);
    });

    $.ajax({
        url: urlCursos,
        method: 'GET',
        success: function(cursos) {
            var cursoSelect = $('#curso');
            cursoSelect.empty();
            if (cursos.ok) {
                $.each(cursos.body, function(index, curso) {
                    cursoSelect.append($('<option>', {
                        value: curso.id,
                        text: curso.nombre 
                    }));
                    cursosMap[curso.id] = curso.nombre;
                });
            } else {
                console.error('Error al cargar cursos:', cursos.message);
            }
        },
        error: function(err) {
            console.error('Error al cargar cursos:', err);
        }
    });

    getTabla(); 
});
