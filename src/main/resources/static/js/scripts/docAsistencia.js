const url = "/v1/docAsistencias";
const urlDocentes = "/v1/docentes";

let docentesMap = {};

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
    const id = $("#guardarAsistencia").data("id");
    const rawDate = $("#fecha").val();

    const asistencia = {
        id,
        fecha: rawDate,
        docenteId: $("#docente").val(),
        estado: $("#asistencia").val() // Se asegura de que la clave coincida con la entidad
    };

    const type = bandera === 1 ? "POST" : "PUT";
    const endpoint = bandera === 1 ? url : `${url}/${id}`;

    ajaxRequest(type, endpoint, asistencia)
        .done((data) => {
            if (data.ok) {
                $("#modal-asistencia").modal("hide");
                getTabla();
                $("#error-message").addClass("d-none");
                Swal.fire({
                    icon: 'success',
                    title: `Se ha ${bandera === 1 ? 'guardado' : 'actualizado'} la asistencia`,
                    showConfirmButton: false,
                    timer: 1500
                });
                clear();
            } else {
                showError(data.message);
            }
        })
        .fail(function(jqXHR) {
            const errorMessage = jqXHR.responseJSON?.message || "Error inesperado. Código: " + jqXHR.status;
            showError(errorMessage);
        });
}

function showError(message) {
    $("#error-message").text(message).removeClass("d-none");
}

function getTabla() {
    ajaxRequest("GET", url)
        .done((data) => {
            const t = $("#tablaAsistencia").DataTable();
            t.clear();

            if (data.ok) {
                $.each(data.body, (index, asistencia) => {
                    const docenteNombre = docentesMap[asistencia.docenteId] || 'Desconocido';
                    const botonera = `
                        <button type="button" class="btn btn-warning btn-xs editar">
                            <i class="fas fa-edit"></i>
                        </button>
                        <button type="button" class="btn btn-danger btn-xs eliminar">
                            <i class="fas fa-trash"></i>
                        </button>`;

                    t.row.add([botonera, asistencia.id, asistencia.fecha, docenteNombre, asistencia.estado]);
                });
                t.draw();
            } else {
                console.error("Error en la respuesta: ", data.message);
            }
        })
        .fail(handleError);
}

function getDocentes() {
    ajaxRequest("GET", urlDocentes)
        .done((data) => {
            if (data.ok) {
                const docenteSelect = $('#docente');
                docenteSelect.empty();
                $.each(data.body, (index, docente) => {
                    // Aseguramos que solo los IDs válidos se añadan al mapa
                    if (docente.id !== 0 && docente.id !== null) {
                        docenteSelect.append($('<option>', {
                            value: docente.id,
                            text: `${docente.apellido}, ${docente.nombre}`
                        }));
                        docentesMap[docente.id] = `${docente.apellido}, ${docente.nombre}`;
                    }
                });

                // Imprimimos el mapa de docentes para ver si se cargó correctamente
                console.log('Docentes:', docentesMap);
            } else {
                console.error('Error al cargar docentes:', data.message);
            }
        })
        .fail(handleError);
}

function clear() {
    $("#fecha").val("");
    $("#docente").val("");
    $("#asistencia").val("presente"); // La posición predeterminada es 'presente'
    $("#guardarAsistencia").removeData("id");
}

function handleError(jqXHR) {
    console.error('Error de AJAX:', jqXHR.statusText);
}

$(document).ready(function() {
    getDocentes();
    getTabla();

    $('#nuevo').click(() => {
        $("#modal-title").text("Nueva Asistencia");
        $("#guardarAsistencia").data("id", null);
        $("#error-message").addClass("d-none");
    });

    $('#guardarAsistencia').click(() => {
        const id = $("#guardarAsistencia").data("id");
        if (id) {
            save(2);
        } else {
            save(1);
        }
    });

    $('#tablaAsistencia').on('click', '.editar', function() {
        const tr = $(this).closest('tr');
        const id = tr.find('td:eq(1)').text();
        ajaxRequest("GET", `${url}/${id}`)
            .done((data) => {
                if (data.ok) {
                    const asistencia = data.body;
                    const adjustedDate = new Date(asistencia.fecha);
                    adjustedDate.setMinutes(adjustedDate.getMinutes() - adjustedDate.getTimezoneOffset());
                    $("#fecha").val(adjustedDate.toISOString().split('T')[0]);
                    $("#docente").val(asistencia.docenteId);
                    $("#asistencia").val(asistencia.estado);
                    $("#guardarAsistencia").data("id", asistencia.id);
                    $("#modal-title").text("Editar Asistencia");
                    $("#modal-asistencia").modal("show");
                } else {
                    console.error("Error al editar: ", data.message);
                }
            })
            .fail(handleError);
    });

    $('#tablaAsistencia').on('click', '.eliminar', function() {
        const id = $(this).closest('tr').find('td:eq(1)').text();
        Swal.fire({
            title: '¿Está seguro de eliminar esta asistencia?',
            showCancelButton: true,
            confirmButtonText: 'Sí, eliminar',
            cancelButtonText: 'Cancelar',
        }).then((result) => {
            if (result.isConfirmed) {
                ajaxRequest("DELETE", `${url}/${id}`)
                    .done((data) => {
                        if (data.ok) {
                            Swal.fire('Eliminado', '', 'success');
                            getTabla();
                        } else {
                            console.error("Error al eliminar: ", data.message);
                        }
                    })
                    .fail(handleError);
            }
        });
    });
});